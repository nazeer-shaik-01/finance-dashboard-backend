package com.nazeer.finance.dto;

import com.nazeer.finance.entity.RecordType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RecordResponse {
    private Long id;
    private BigDecimal amount;
    private RecordType type;
    private String category;
    private String description;
    private LocalDate transactionDate;

    public RecordResponse(Long id, BigDecimal amount, RecordType type, String category, String description, LocalDate transactionDate) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.description = description;
        this.transactionDate = transactionDate;
    }

    public Long getId() { return id; }
    public BigDecimal getAmount() { return amount; }
    public RecordType getType() { return type; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public LocalDate getTransactionDate() { return transactionDate; }
}
