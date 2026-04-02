package com.nazeer.finance.dto;

import com.nazeer.finance.entity.RecordType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RecordRequest {
    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private RecordType type;

    @NotBlank
    private String category;

    private String description;

    @NotNull
    private LocalDate transactionDate;

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public RecordType getType() { return type; }
    public void setType(RecordType type) { this.type = type; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }
}
