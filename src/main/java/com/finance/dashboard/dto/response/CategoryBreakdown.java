package com.finance.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryBreakdown {
    private Map<String, CategoryAmount> incomeByCategory;
    private Map<String, CategoryAmount> expenseByCategory;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryAmount {
        private BigDecimal amount;
        private double percentage;
    }
}
