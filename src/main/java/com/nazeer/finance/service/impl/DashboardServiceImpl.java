package com.nazeer.finance.service.impl;

import com.nazeer.finance.dto.CategoryTotalResponse;
import com.nazeer.finance.dto.DashboardSummaryResponse;
import com.nazeer.finance.dto.RecordResponse;
import com.nazeer.finance.dto.TrendPointResponse;
import com.nazeer.finance.entity.FinancialRecord;
import com.nazeer.finance.entity.RecordType;
import com.nazeer.finance.repository.FinancialRecordRepository;
import com.nazeer.finance.service.DashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final FinancialRecordRepository financialRecordRepository;

    public DashboardServiceImpl(FinancialRecordRepository financialRecordRepository) {
        this.financialRecordRepository = financialRecordRepository;
    }

    @Override
    public DashboardSummaryResponse getSummary() {
        List<FinancialRecord> records = financialRecordRepository.findAll();
        BigDecimal income = sumByType(records, RecordType.INCOME);
        BigDecimal expense = sumByType(records, RecordType.EXPENSE);
        return new DashboardSummaryResponse(income, expense, income.subtract(expense));
    }

    @Override
    public List<CategoryTotalResponse> getCategoryTotals() {
        Map<String, BigDecimal> totals = financialRecordRepository.findAll().stream()
                .collect(Collectors.groupingBy(FinancialRecord::getCategory,
                        Collectors.mapping(FinancialRecord::getAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));

        return totals.entrySet().stream()
                .map(e -> new CategoryTotalResponse(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(CategoryTotalResponse::getCategory))
                .collect(Collectors.toList());
    }

    @Override
    public List<TrendPointResponse> getTrends(String granularity) {
        List<FinancialRecord> records = financialRecordRepository.findAll();
        Map<String, List<FinancialRecord>> grouped = records.stream()
                .collect(Collectors.groupingBy(r -> bucket(r.getTransactionDate(), granularity)));

        return grouped.entrySet().stream()
                .map(e -> new TrendPointResponse(e.getKey(), sumByType(e.getValue(), RecordType.INCOME), sumByType(e.getValue(), RecordType.EXPENSE)))
                .sorted(Comparator.comparing(TrendPointResponse::getPeriod))
                .collect(Collectors.toList());
    }

    @Override
    public List<RecordResponse> getRecentTransactions(int limit) {
        return financialRecordRepository.findAll().stream()
                .sorted(Comparator.comparing(FinancialRecord::getTransactionDate).reversed())
                .limit(limit)
                .map(record -> new RecordResponse(record.getId(), record.getAmount(), record.getType(), record.getCategory(), record.getDescription(), record.getTransactionDate()))
                .collect(Collectors.toList());
    }

    private BigDecimal sumByType(List<FinancialRecord> records, RecordType type) {
        return records.stream().filter(r -> r.getType() == type).map(FinancialRecord::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String bucket(LocalDate date, String granularity) {
        if ("weekly".equalsIgnoreCase(granularity)) {
            return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toString();
        }
        return String.format("%d-%02d", date.getYear(), date.getMonthValue());
    }
}
