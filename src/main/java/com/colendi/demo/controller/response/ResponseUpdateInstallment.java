package com.colendi.demo.controller.response;

import com.colendi.demo.enums.InstallmentStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
public class ResponseUpdateInstallment {
    private Long id;
    private InstallmentStatus status;
    private BigDecimal amount;
    private Timestamp dueDate;
}
