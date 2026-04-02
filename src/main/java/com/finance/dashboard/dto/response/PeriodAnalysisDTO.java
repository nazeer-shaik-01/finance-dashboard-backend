package com.finance.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PeriodAnalysisDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netBalance;
    private BigDecimal averageDailyIncome;
    private BigDecimal averageDailyExpense;
    private String topCategory;
}
