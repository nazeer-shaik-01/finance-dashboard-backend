package com.finance.dashboard.controller;

import com.finance.dashboard.dto.request.DateRangeFilter;
import com.finance.dashboard.dto.request.FinancialRecordRequest;
import com.finance.dashboard.dto.response.FinancialRecordResponse;
import com.finance.dashboard.entity.User;
import com.finance.dashboard.exception.UnauthorizedException;
import com.finance.dashboard.security.SecurityUtils;
import com.finance.dashboard.service.DashboardService;
import com.finance.dashboard.service.FinancialRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
@Tag(name = "Financial Records", description = "Financial record management")
public class FinancialRecordController {

    private final FinancialRecordService recordService;
    private final DashboardService dashboardService;
    private final SecurityUtils securityUtils;

    @GetMapping
    @Operation(summary = "Get records for current user with pagination")
    public ResponseEntity<Page<FinancialRecordResponse>> getRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy) {
        Long userId = getCurrentUserId();
        Sort sort = sortBy != null ? Sort.by(sortBy).descending() : Sort.by("recordDate").descending();
        return ResponseEntity.ok(recordService.getRecordsByUser(userId, PageRequest.of(page, size, sort)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    @Operation(summary = "Create a new financial record")
    public ResponseEntity<FinancialRecordResponse> createRecord(@Valid @RequestBody FinancialRecordRequest request) {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(recordService.createRecord(request, userId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get record by ID")
    public ResponseEntity<FinancialRecordResponse> getRecordById(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(recordService.getRecordById(id, userId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    @Operation(summary = "Update a financial record")
    public ResponseEntity<FinancialRecordResponse> updateRecord(
            @PathVariable Long id, @Valid @RequestBody FinancialRecordRequest request) {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(recordService.updateRecord(id, request, userId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    @Operation(summary = "Soft delete a financial record")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        recordService.deleteRecord(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    @Operation(summary = "Filter records by date range, type, and category")
    public ResponseEntity<List<FinancialRecordResponse>> filterRecords(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long categoryId) {
        Long userId = getCurrentUserId();
        DateRangeFilter filter = new DateRangeFilter();
        filter.setStartDate(startDate);
        filter.setEndDate(endDate);
        filter.setType(type);
        filter.setCategoryId(categoryId);
        return ResponseEntity.ok(recordService.filterRecords(userId, filter));
    }

    @GetMapping("/recent")
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
