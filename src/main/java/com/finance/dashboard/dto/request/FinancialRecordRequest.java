package com.finance.dashboard.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class FinancialRecordRequest {
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Type is required")
    private String type;

    @NotNull(message = "Category is required")
    private Long categoryId;

    @NotNull(message = "Record date is required")
    private LocalDate recordDate;

    private String notes;
}
