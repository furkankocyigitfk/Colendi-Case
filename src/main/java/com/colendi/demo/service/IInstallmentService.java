package com.colendi.demo.service;

import com.colendi.demo.controller.request.RequestUpdateInstallment;
import com.colendi.demo.controller.response.ResponseUpdateInstallment;

public interface IInstallmentService {
    ResponseUpdateInstallment update(RequestUpdateInstallment request);
}
