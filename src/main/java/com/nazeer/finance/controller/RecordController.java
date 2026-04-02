package com.nazeer.finance.controller;

import com.nazeer.finance.dto.RecordRequest;
import com.nazeer.finance.dto.RecordResponse;
import com.nazeer.finance.entity.RecordType;
import com.nazeer.finance.service.RecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/records")
public class RecordController {

    private final RecordService recordService;

    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
    public ResponseEntity<List<RecordResponse>> getAllRecords() {
        return ResponseEntity.ok(recordService.getRecords());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public ResponseEntity<RecordResponse> createRecord(@Valid @RequestBody RecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recordService.createRecord(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public ResponseEntity<RecordResponse> updateRecord(@PathVariable Long id, @Valid @RequestBody RecordRequest request) {
        return ResponseEntity.ok(recordService.updateRecord(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
    public ResponseEntity<List<RecordResponse>> filterRecords(
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) RecordType type) {
        return ResponseEntity.ok(recordService.filterRecords(fromDate, toDate, category, type));
    }
}
