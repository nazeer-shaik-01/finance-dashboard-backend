package com.finance.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialRecordResponse {
    private Long id;
    private Long userId;
    private BigDecimal amount;
    private String type;
    private CategoryResponse category;
    private LocalDate recordDate;
    private String notes;
    private LocalDateTime createdAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryResponse {
        private Long id;
        private String name;
        private String type;
        private String icon;
    }
}
