package com.finance.dashboard.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public final class DateUtils {
    private DateUtils() {}

    public static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    public static String formatMonth(LocalDate date) {
        return date.format(MONTH_FORMATTER);
    }

    public static LocalDate getStartOfMonth(YearMonth yearMonth) {
        return yearMonth.atDay(1);
    }

    public static LocalDate getEndOfMonth(YearMonth yearMonth) {
        return yearMonth.atEndOfMonth();
    }

    public static long daysBetween(LocalDate start, LocalDate end) {
        return Math.max(1, end.toEpochDay() - start.toEpochDay() + 1);
    }
}
