package com.nazeer.finance.service;

import com.nazeer.finance.dto.RecordRequest;
import com.nazeer.finance.dto.RecordResponse;
import com.nazeer.finance.entity.RecordType;

import java.time.LocalDate;
import java.util.List;

public interface RecordService {
    RecordResponse createRecord(RecordRequest request);
    List<RecordResponse> getRecords();
    RecordResponse updateRecord(Long id, RecordRequest request);
    void deleteRecord(Long id);
    List<RecordResponse> filterRecords(LocalDate fromDate, LocalDate toDate, String category, RecordType type);
}
