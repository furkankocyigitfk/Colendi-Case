package com.colendi.demo.controller.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RequestUpdateInstallment {
    @NotNull(message = "Installment id cannot be null")
    private Long installmentId;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount should be positive")
    private BigDecimal amount;
}
