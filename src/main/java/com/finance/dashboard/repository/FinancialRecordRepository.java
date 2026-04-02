package com.finance.dashboard.repository;

import com.finance.dashboard.entity.FinancialRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {

    List<FinancialRecord> findByUserIdAndDeletedAtIsNull(Long userId);

    Page<FinancialRecord> findByUserIdAndDeletedAtIsNull(Long userId, Pageable pageable);

    List<FinancialRecord> findByUserIdAndRecordDateBetweenAndDeletedAtIsNull(
            Long userId, LocalDate startDate, LocalDate endDate);

    List<FinancialRecord> findByUserIdAndTypeAndDeletedAtIsNull(
            Long userId, FinancialRecord.RecordType type);

    List<FinancialRecord> findByUserIdAndCategoryIdAndDeletedAtIsNull(
            Long userId, Long categoryId);

    Optional<FinancialRecord> findByIdAndUserIdAndDeletedAtIsNull(Long id, Long userId);

    @Query("SELECT SUM(r.amount) FROM FinancialRecord r WHERE r.user.id = :userId AND r.type = :type AND r.deletedAt IS NULL")
    BigDecimal sumAmountByUserIdAndType(@Param("userId") Long userId, @Param("type") FinancialRecord.RecordType type);

    @Query("SELECT COUNT(r) FROM FinancialRecord r WHERE r.user.id = :userId AND r.deletedAt IS NULL")
    long countByUserIdAndDeletedAtIsNull(@Param("userId") Long userId);

    @Query("SELECT r FROM FinancialRecord r WHERE r.user.id = :userId AND r.deletedAt IS NULL ORDER BY r.recordDate DESC, r.createdAt DESC")
    List<FinancialRecord> findRecentByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT r FROM FinancialRecord r WHERE r.user.id = :userId AND r.recordDate BETWEEN :startDate AND :endDate AND r.deletedAt IS NULL AND (:type IS NULL OR r.type = :type) AND (:categoryId IS NULL OR r.category.id = :categoryId)")
    List<FinancialRecord> findByUserIdAndFilters(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("type") FinancialRecord.RecordType type,
            @Param("categoryId") Long categoryId);
}
