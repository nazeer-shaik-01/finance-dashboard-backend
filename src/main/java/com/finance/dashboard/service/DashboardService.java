package com.finance.dashboard.service;

import com.finance.dashboard.dto.response.*;
import com.finance.dashboard.entity.FinancialRecord;
import com.finance.dashboard.repository.FinancialRecordRepository;
import com.finance.dashboard.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final FinancialRecordRepository recordRepository;
    private final FinancialRecordService financialRecordService;

    public DashboardSummary getSummary(Long userId) {
        BigDecimal totalIncome = Optional.ofNullable(
                recordRepository.sumAmountByUserIdAndType(userId, FinancialRecord.RecordType.INCOME))
                .orElse(BigDecimal.ZERO);
        BigDecimal totalExpenses = Optional.ofNullable(
                recordRepository.sumAmountByUserIdAndType(userId, FinancialRecord.RecordType.EXPENSE))
                .orElse(BigDecimal.ZERO);
        long count = recordRepository.countByUserIdAndDeletedAtIsNull(userId);
        return DashboardSummary.builder()
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .netBalance(totalIncome.subtract(totalExpenses))
                .recordCount(count)
                .lastUpdated(LocalDateTime.now())
                .build();
    }

    public CategoryBreakdown getCategoryBreakdown(Long userId) {
        List<FinancialRecord> allRecords = recordRepository.findByUserIdAndDeletedAtIsNull(userId);

        Map<String, BigDecimal> incomeMap = new LinkedHashMap<>();
        Map<String, BigDecimal> expenseMap = new LinkedHashMap<>();
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;

        for (FinancialRecord r : allRecords) {
            String catName = r.getCategory() != null ? r.getCategory().getName() : "Uncategorized";
            if (r.getType() == FinancialRecord.RecordType.INCOME) {
                incomeMap.merge(catName, r.getAmount(), BigDecimal::add);
                totalIncome = totalIncome.add(r.getAmount());
            } else {
                expenseMap.merge(catName, r.getAmount(), BigDecimal::add);
                totalExpense = totalExpense.add(r.getAmount());
            }
        }

        final BigDecimal finalIncome = totalIncome;
        final BigDecimal finalExpense = totalExpense;

        Map<String, CategoryBreakdown.CategoryAmount> incomeBreakdown = incomeMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> CategoryBreakdown.CategoryAmount.builder()
                                .amount(e.getValue())
                                .percentage(calculatePercentage(e.getValue(), finalIncome))
                                .build(),
                        (a, b) -> a, LinkedHashMap::new));

        Map<String, CategoryBreakdown.CategoryAmount> expenseBreakdown = expenseMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> CategoryBreakdown.CategoryAmount.builder()
                                .amount(e.getValue())
                                .percentage(calculatePercentage(e.getValue(), finalExpense))
                                .build(),
                        (a, b) -> a, LinkedHashMap::new));

        return CategoryBreakdown.builder()
                .incomeByCategory(incomeBreakdown)
                .expenseByCategory(expenseBreakdown)
                .build();
    }

    public List<MonthlyTrend> getMonthlyTrends(Long userId, int months) {
        List<MonthlyTrend> trends = new ArrayList<>();
        YearMonth current = YearMonth.now();
        for (int i = months - 1; i >= 0; i--) {
            YearMonth ym = current.minusMonths(i);
            LocalDate start = DateUtils.getStartOfMonth(ym);
            LocalDate end = DateUtils.getEndOfMonth(ym);
            List<FinancialRecord> records = recordRepository
                    .findByUserIdAndRecordDateBetweenAndDeletedAtIsNull(userId, start, end);
            BigDecimal income = records.stream()
                    .filter(r -> r.getType() == FinancialRecord.RecordType.INCOME)
                    .map(FinancialRecord::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal expense = records.stream()
                    .filter(r -> r.getType() == FinancialRecord.RecordType.EXPENSE)
                    .map(FinancialRecord::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            trends.add(MonthlyTrend.builder()
                    .month(DateUtils.formatMonth(start))
                    .income(income)
                    .expenses(expense)
                    .netChange(income.subtract(expense))
                    .build());
        }
        return trends;
    }

    public List<FinancialRecordResponse> getRecentTransactions(Long userId, int limit) {
        return recordRepository.findRecentByUserId(userId, PageRequest.of(0, limit))
                .stream().map(financialRecordService::mapToResponse).collect(Collectors.toList());
    }

    public PeriodAnalysisDTO getPeriodAnalysis(Long userId, LocalDate startDate, LocalDate endDate) {
        List<FinancialRecord> records = recordRepository
                .findByUserIdAndRecordDateBetweenAndDeletedAtIsNull(userId, startDate, endDate);

        BigDecimal totalIncome = records.stream()
                .filter(r -> r.getType() == FinancialRecord.RecordType.INCOME)
                .map(FinancialRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = records.stream()
                .filter(r -> r.getType() == FinancialRecord.RecordType.EXPENSE)
                .map(FinancialRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long days = DateUtils.daysBetween(startDate, endDate);

        BigDecimal avgDailyIncome = totalIncome.divide(BigDecimal.valueOf(days), 2, RoundingMode.HALF_UP);
        BigDecimal avgDailyExpense = totalExpenses.divide(BigDecimal.valueOf(days), 2, RoundingMode.HALF_UP);

        String topCategory = records.stream()
                .filter(r -> r.getCategory() != null)
                .collect(Collectors.groupingBy(
                        r -> r.getCategory().getName(),
                        Collectors.reducing(BigDecimal.ZERO, FinancialRecord::getAmount, BigDecimal::add)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        return PeriodAnalysisDTO.builder()
                .startDate(startDate)
                .endDate(endDate)
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .netBalance(totalIncome.subtract(totalExpenses))
                .averageDailyIncome(avgDailyIncome)
                .averageDailyExpense(avgDailyExpense)
                .topCategory(topCategory)
                .build();
    }

    private double calculatePercentage(BigDecimal amount, BigDecimal total) {
        if (total.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return amount.multiply(BigDecimal.valueOf(100))
                .divide(total, 2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
