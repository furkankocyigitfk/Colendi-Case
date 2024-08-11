package com.colendi.demo.controller.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class RequestCreateCredit {
    @NotNull(message = "User id cannot be null")
    private Long userId;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount should be positive")
    private BigDecimal amount;
    @NotNull(message = "Installment count cannot be null")
    @Positive(message = "Installment count should be positive")
    private Integer installmentCount;

}
