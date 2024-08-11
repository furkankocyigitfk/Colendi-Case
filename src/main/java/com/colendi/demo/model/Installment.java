package com.colendi.demo.model;

import com.colendi.demo.enums.InstallmentStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "INSTALLMENT")
@Getter
@Setter
public class Installment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Timestamp createDate;
    @UpdateTimestamp
    private Timestamp updateDate;
    @Enumerated
    @Column(name = "status")
    private InstallmentStatus installmentStatus;
    private BigDecimal amount;
    private Timestamp dueDate;

    @ManyToOne
    @JoinColumn(name = "credit_id", nullable = false)
    @JsonBackReference
    private Credit credit;
}
