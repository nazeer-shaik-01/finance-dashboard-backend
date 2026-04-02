package com.finance.dashboard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_records", indexes = {
    @Index(name = "idx_user_deleted", columnList = "user_id, deletedAt"),
    @Index(name = "idx_record_date", columnList = "recordDate"),
    @Index(name = "idx_category", columnList = "category_id")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecordType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private LocalDate recordDate;

    @Column(length = 1000)
    private String notes;

    private LocalDateTime deletedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum RecordType {
        INCOME, EXPENSE
    }
}
