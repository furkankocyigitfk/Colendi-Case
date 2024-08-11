package com.colendi.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
public class InstallmentDto {
    private Long id;
    private BigDecimal amount;
    private Timestamp dueDate;
}
