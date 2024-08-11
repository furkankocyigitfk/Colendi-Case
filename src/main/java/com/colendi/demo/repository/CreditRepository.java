package com.colendi.demo.repository;

import com.colendi.demo.model.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long>, JpaSpecificationExecutor<Credit> {
}
