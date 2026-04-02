package com.nazeer.finance.dto;

import java.math.BigDecimal;

public class CategoryTotalResponse {
    private String category;
    private BigDecimal total;

    public CategoryTotalResponse(String category, BigDecimal total) {
        this.category = category;
        this.total = total;
    }

    public String getCategory() { return category; }
    public BigDecimal getTotal() { return total; }
}
