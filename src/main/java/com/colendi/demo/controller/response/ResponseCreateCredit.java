package com.colendi.demo.controller.response;

import com.colendi.demo.dto.InstallmentDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseCreateCredit {
    private Long creditId;
    private List<InstallmentDto> installments;
}
