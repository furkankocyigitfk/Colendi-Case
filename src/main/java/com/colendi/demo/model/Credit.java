package com.colendi.demo.model;

import com.colendi.demo.enums.CreditStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "CREDIT")
@Getter
@Setter
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Timestamp createDate;
    @UpdateTimestamp
    private Timestamp updateDate;
    @Enumerated
    @Column(name = "status")
    private CreditStatus creditStatus;
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "credit", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Installment> installments;
}
