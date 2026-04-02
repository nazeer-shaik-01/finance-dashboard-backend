package com.nazeer.finance.dto;

import java.math.BigDecimal;

public class TrendPointResponse {
    private String period;
    private BigDecimal income;
    private BigDecimal expense;

    public TrendPointResponse(String period, BigDecimal income, BigDecimal expense) {
        this.period = period;
        this.income = income;
        this.expense = expense;
    }

    public String getPeriod() { return period; }
    public BigDecimal getIncome() { return income; }
    public BigDecimal getExpense() { return expense; }
}
