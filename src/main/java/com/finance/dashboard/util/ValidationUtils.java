package com.finance.dashboard.util;

import com.finance.dashboard.exception.ValidationException;

import java.time.LocalDate;

public final class ValidationUtils {
    private ValidationUtils() {}

    public static void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new ValidationException("Start date cannot be after end date");
        }
    }

    public static void validatePositiveLimit(int limit) {
        if (limit <= 0 || limit > 1000) {
            throw new ValidationException("Limit must be between 1 and 1000");
        }
    }
}
