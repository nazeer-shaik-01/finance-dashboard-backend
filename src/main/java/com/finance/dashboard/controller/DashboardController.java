package com.finance.dashboard.controller;

import com.finance.dashboard.dto.response.*;
import com.finance.dashboard.entity.User;
import com.finance.dashboard.exception.UnauthorizedException;
import com.finance.dashboard.security.SecurityUtils;
import com.finance.dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Dashboard analytics endpoints")
public class DashboardController {

    private final DashboardService dashboardService;
    private final SecurityUtils securityUtils;

    @GetMapping("/summary")
    @Operation(summary = "Get dashboard summary")
    public ResponseEntity<DashboardSummary> getSummary() {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(dashboardService.getSummary(userId));
    }

    @GetMapping("/category-breakdown")
    @Operation(summary = "Get category breakdown")
    public ResponseEntity<CategoryBreakdown> getCategoryBreakdown() {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(dashboardService.getCategoryBreakdown(userId));
    }

    @GetMapping("/monthly-trends")
    @Operation(summary = "Get monthly trends")
    public ResponseEntity<List<MonthlyTrend>> getMonthlyTrends(
            @RequestParam(defaultValue = "12") int months) {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(dashboardService.getMonthlyTrends(userId, months));
    }

    @GetMapping("/period-analysis")
    @Operation(summary = "Get period analysis")
    public ResponseEntity<PeriodAnalysisDTO> getPeriodAnalysis(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(dashboardService.getPeriodAnalysis(userId, startDate, endDate));
    }

    @GetMapping("/recent-transactions")
    @Operation(summary = "Get recent transactions")
    public ResponseEntity<List<FinancialRecordResponse>> getRecentTransactions(
            @RequestParam(defaultValue = "10") int limit) {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(dashboardService.getRecentTransactions(userId, limit));
    }

    private Long getCurrentUserId() {
        return securityUtils.getCurrentUser()
                .map(User::getId)
                .orElseThrow(() -> new UnauthorizedException("Not authenticated"));
    }
}
