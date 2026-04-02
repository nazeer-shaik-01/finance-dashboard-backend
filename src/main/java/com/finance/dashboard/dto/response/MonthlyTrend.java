package com.finance.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyTrend {
    private String month;
    private BigDecimal income;
    private BigDecimal expenses;
    private BigDecimal netChange;
}
