package com.nazeer.finance.controller;

import com.nazeer.finance.dto.CategoryTotalResponse;
import com.nazeer.finance.dto.DashboardSummaryResponse;
import com.nazeer.finance.dto.RecordResponse;
import com.nazeer.finance.dto.TrendPointResponse;
import com.nazeer.finance.service.DashboardService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@Validated
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
    public ResponseEntity<DashboardSummaryResponse> getSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }

    @GetMapping("/category")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
    public ResponseEntity<List<CategoryTotalResponse>> getCategoryTotals() {
        return ResponseEntity.ok(dashboardService.getCategoryTotals());
    }

    @GetMapping("/trends")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
    public ResponseEntity<List<TrendPointResponse>> getTrends(@RequestParam(defaultValue = "monthly") String granularity) {
        return ResponseEntity.ok(dashboardService.getTrends(granularity));
    }

    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
    public ResponseEntity<List<RecordResponse>> getRecent(
            @RequestParam(defaultValue = "5") @Min(1) @Max(100) int limit) {
        return ResponseEntity.ok(dashboardService.getRecentTransactions(limit));
    }
}
