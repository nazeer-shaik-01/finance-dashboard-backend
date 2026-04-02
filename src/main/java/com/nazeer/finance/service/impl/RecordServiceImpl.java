package com.nazeer.finance.service.impl;

import com.nazeer.finance.dto.RecordRequest;
import com.nazeer.finance.dto.RecordResponse;
import com.nazeer.finance.entity.FinancialRecord;
import com.nazeer.finance.entity.RecordType;
import com.nazeer.finance.exception.ResourceNotFoundException;
import com.nazeer.finance.repository.FinancialRecordRepository;
import com.nazeer.finance.service.RecordService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecordServiceImpl implements RecordService {

    private final FinancialRecordRepository financialRecordRepository;

    public RecordServiceImpl(FinancialRecordRepository financialRecordRepository) {
        this.financialRecordRepository = financialRecordRepository;
    }

    @Override
    public RecordResponse createRecord(RecordRequest request) {
        FinancialRecord record = new FinancialRecord();
        applyRequest(record, request);
        return toResponse(financialRecordRepository.save(record));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecordResponse> getRecords() {
        return financialRecordRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public RecordResponse updateRecord(Long id, RecordRequest request) {
        FinancialRecord record = financialRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found"));
        applyRequest(record, request);
        return toResponse(financialRecordRepository.save(record));
    }

    @Override
    public void deleteRecord(Long id) {
        if (!financialRecordRepository.existsById(id)) {
            throw new ResourceNotFoundException("Record not found");
        }
        financialRecordRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecordResponse> filterRecords(LocalDate fromDate, LocalDate toDate, String category, RecordType type) {
        Specification<FinancialRecord> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (fromDate != null) predicates.add(cb.greaterThanOrEqualTo(root.get("transactionDate"), fromDate));
            if (toDate != null) predicates.add(cb.lessThanOrEqualTo(root.get("transactionDate"), toDate));
            if (category != null && !category.isBlank()) predicates.add(cb.equal(cb.lower(root.get("category")), category.toLowerCase()));
            if (type != null) predicates.add(cb.equal(root.get("type"), type));
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return financialRecordRepository.findAll(spec).stream().map(this::toResponse).collect(Collectors.toList());
    }

    private void applyRequest(FinancialRecord record, RecordRequest request) {
        record.setAmount(request.getAmount());
        record.setType(request.getType());
        record.setCategory(request.getCategory());
        record.setDescription(request.getDescription());
        record.setTransactionDate(request.getTransactionDate());
    }

    private RecordResponse toResponse(FinancialRecord record) {
        return new RecordResponse(record.getId(), record.getAmount(), record.getType(), record.getCategory(), record.getDescription(), record.getTransactionDate());
    }
}
