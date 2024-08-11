package com.colendi.demo.controller;

import com.colendi.demo.controller.request.RequestCreateCredit;
import com.colendi.demo.controller.response.ResponseCreateCredit;
import com.colendi.demo.enums.CreditStatus;
import com.colendi.demo.model.Credit;
import com.colendi.demo.service.ICreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequestMapping("/api/credit")
@RequiredArgsConstructor
@RestController
public class CreditController {
    private final ICreditService creditService;

    @PostMapping("/create")
    ResponseEntity<ResponseCreateCredit> create(@RequestBody @Validated RequestCreateCredit request) {
        return ResponseEntity.ok(creditService.create(request));
    }

    @GetMapping("/getByUserId")
    public ResponseEntity<Page<Credit>> getByUserId(
            @RequestParam Long userId,
            @RequestParam(required = false) CreditStatus status,
            @RequestParam(name = "createDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(creditService.getByUserId(userId, status, createDate, page, size));
    }
}