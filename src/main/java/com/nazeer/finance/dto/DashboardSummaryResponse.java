package com.nazeer.finance.dto;

import java.math.BigDecimal;

public class DashboardSummaryResponse {
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netBalance;

    public DashboardSummaryResponse(BigDecimal totalIncome, BigDecimal totalExpenses, BigDecimal netBalance) {
        this.totalIncome = totalIncome;
        this.totalExpenses = totalExpenses;
        this.netBalance = netBalance;
    }

    public BigDecimal getTotalIncome() { return totalIncome; }
    public BigDecimal getTotalExpenses() { return totalExpenses; }
    public BigDecimal getNetBalance() { return netBalance; }
}
