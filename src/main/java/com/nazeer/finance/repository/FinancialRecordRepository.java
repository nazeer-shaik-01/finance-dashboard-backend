package com.nazeer.finance.repository;

import com.nazeer.finance.entity.FinancialRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long>, JpaSpecificationExecutor<FinancialRecord> {
}
