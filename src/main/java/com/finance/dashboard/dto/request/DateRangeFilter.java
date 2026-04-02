package com.finance.dashboard.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DateRangeFilter {
    private LocalDate startDate;
    private LocalDate endDate;
    private String type;
    private Long categoryId;
}
