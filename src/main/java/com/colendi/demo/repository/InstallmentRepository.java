package com.colendi.demo.repository;

import com.colendi.demo.enums.InstallmentStatus;
import com.colendi.demo.model.Installment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface InstallmentRepository extends JpaRepository<Installment, Long> {
    List<Installment> findByInstallmentStatusAndDueDateBefore(InstallmentStatus installmentStatus, Timestamp now);
}
