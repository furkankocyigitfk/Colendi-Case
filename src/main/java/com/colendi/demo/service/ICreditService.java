package com.colendi.demo.service;

import com.colendi.demo.controller.request.RequestCreateCredit;
import com.colendi.demo.controller.response.ResponseCreateCredit;
import com.colendi.demo.enums.CreditStatus;
import com.colendi.demo.model.Credit;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface ICreditService {
    ResponseCreateCredit create(RequestCreateCredit request);

    Page<Credit> getByUserId(Long userId, CreditStatus status, LocalDateTime createDate, int page, int size);

    void save(Credit credit);
}
