package com.nazeer.finance.service;

import com.nazeer.finance.dto.CategoryTotalResponse;
import com.nazeer.finance.dto.DashboardSummaryResponse;
import com.nazeer.finance.dto.RecordResponse;
import com.nazeer.finance.dto.TrendPointResponse;

import java.util.List;

public interface DashboardService {
    DashboardSummaryResponse getSummary();
    List<CategoryTotalResponse> getCategoryTotals();
    List<TrendPointResponse> getTrends(String granularity);
    List<RecordResponse> getRecentTransactions(int limit);
}
