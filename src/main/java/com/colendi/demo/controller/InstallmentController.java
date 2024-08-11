package com.colendi.demo.controller;

import com.colendi.demo.controller.request.RequestUpdateInstallment;
import com.colendi.demo.controller.response.ResponseUpdateInstallment;
import com.colendi.demo.service.IInstallmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/installment")
@RequiredArgsConstructor
@RestController
public class InstallmentController {
    private final IInstallmentService installmentService;

    @PutMapping("/update")
    ResponseEntity<ResponseUpdateInstallment> update(@RequestBody @Validated RequestUpdateInstallment request) {
        return ResponseEntity.ok(installmentService.update(request));
    }

}
