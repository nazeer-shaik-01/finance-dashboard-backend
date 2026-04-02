package com.finance.dashboard.util;

public final class Constants {
    private Constants() {}

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_ANALYST = "ANALYST";
    public static final String ROLE_VIEWER = "VIEWER";

    public static final String TYPE_INCOME = "INCOME";
    public static final String TYPE_EXPENSE = "EXPENSE";

    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INACTIVE = "INACTIVE";
    public static final String STATUS_SUSPENDED = "SUSPENDED";

    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";

    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 100;
}
