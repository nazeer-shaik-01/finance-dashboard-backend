package com.finance.dashboard.service;

import com.finance.dashboard.dto.request.DateRangeFilter;
import com.finance.dashboard.dto.request.FinancialRecordRequest;
import com.finance.dashboard.dto.response.FinancialRecordResponse;
import com.finance.dashboard.entity.Category;
import com.finance.dashboard.entity.FinancialRecord;
import com.finance.dashboard.entity.User;
import com.finance.dashboard.exception.ResourceNotFoundException;
import com.finance.dashboard.exception.ValidationException;
import com.finance.dashboard.repository.CategoryRepository;
import com.finance.dashboard.repository.FinancialRecordRepository;
import com.finance.dashboard.repository.UserRepository;
import com.finance.dashboard.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinancialRecordService {

    private final FinancialRecordRepository recordRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public FinancialRecordResponse createRecord(FinancialRecordRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));
        FinancialRecord.RecordType type;
        try {
            type = FinancialRecord.RecordType.valueOf(request.getType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid record type: " + request.getType());
        }
        FinancialRecord record = FinancialRecord.builder()
                .user(user)
                .amount(request.getAmount())
                .type(type)
                .category(category)
                .recordDate(request.getRecordDate())
                .notes(request.getNotes())
                .build();
        record = recordRepository.save(record);
        log.info("Financial record created for user {}: {}", userId, record.getId());
        return mapToResponse(record);
    }

    public Page<FinancialRecordResponse> getRecordsByUser(Long userId, Pageable pageable) {
        return recordRepository.findByUserIdAndDeletedAtIsNull(userId, pageable)
                .map(this::mapToResponse);
    }

    public FinancialRecordResponse getRecordById(Long id, Long userId) {
        FinancialRecord record = recordRepository.findByIdAndUserIdAndDeletedAtIsNull(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("FinancialRecord", id));
        return mapToResponse(record);
    }

    @Transactional
    public FinancialRecordResponse updateRecord(Long id, FinancialRecordRequest request, Long userId) {
        FinancialRecord record = recordRepository.findByIdAndUserIdAndDeletedAtIsNull(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("FinancialRecord", id));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));
        FinancialRecord.RecordType type;
        try {
            type = FinancialRecord.RecordType.valueOf(request.getType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid record type: " + request.getType());
        }
        record.setAmount(request.getAmount());
        record.setType(type);
        record.setCategory(category);
        record.setRecordDate(request.getRecordDate());
        record.setNotes(request.getNotes());
        record = recordRepository.save(record);
        log.info("Financial record {} updated for user {}", id, userId);
        return mapToResponse(record);
    }

    @Transactional
    public void deleteRecord(Long id, Long userId) {
        FinancialRecord record = recordRepository.findByIdAndUserIdAndDeletedAtIsNull(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("FinancialRecord", id));
        record.setDeletedAt(LocalDateTime.now());
        recordRepository.save(record);
        log.info("Financial record {} soft-deleted for user {}", id, userId);
    }

    public List<FinancialRecordResponse> filterRecords(Long userId, DateRangeFilter filter) {
        ValidationUtils.validateDateRange(filter.getStartDate(), filter.getEndDate());
        FinancialRecord.RecordType type = null;
        if (filter.getType() != null) {
            try {
                type = FinancialRecord.RecordType.valueOf(filter.getType().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ValidationException("Invalid type: " + filter.getType());
            }
        }
        return recordRepository.findByUserIdAndFilters(
                        userId,
                        filter.getStartDate(),
                        filter.getEndDate(),
                        type,
                        filter.getCategoryId())
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<FinancialRecordResponse> getRecordsByType(Long userId, String type) {
        FinancialRecord.RecordType recordType;
        try {
            recordType = FinancialRecord.RecordType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid type: " + type);
        }
        return recordRepository.findByUserIdAndTypeAndDeletedAtIsNull(userId, recordType)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<FinancialRecordResponse> getRecordsByCategory(Long userId, Long categoryId) {
        return recordRepository.findByUserIdAndCategoryIdAndDeletedAtIsNull(userId, categoryId)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public FinancialRecordResponse mapToResponse(FinancialRecord record) {
        FinancialRecordResponse.CategoryResponse categoryResponse = null;
        if (record.getCategory() != null) {
            categoryResponse = FinancialRecordResponse.CategoryResponse.builder()
                    .id(record.getCategory().getId())
                    .name(record.getCategory().getName())
                    .type(record.getCategory().getType().name())
                    .icon(record.getCategory().getIcon())
                    .build();
        }
        return FinancialRecordResponse.builder()
                .id(record.getId())
                .userId(record.getUser().getId())
                .amount(record.getAmount())
                .type(record.getType().name())
                .category(categoryResponse)
                .recordDate(record.getRecordDate())
                .notes(record.getNotes())
                .createdAt(record.getCreatedAt())
                .build();
    }
}
