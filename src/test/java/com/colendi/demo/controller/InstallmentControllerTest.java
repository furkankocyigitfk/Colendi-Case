package com.colendi.demo.controller;

import com.colendi.demo.controller.request.RequestUpdateInstallment;
import com.colendi.demo.controller.response.ResponseUpdateInstallment;
import com.colendi.demo.service.IInstallmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InstallmentController.class)
@RunWith(SpringRunner.class)
public class InstallmentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IInstallmentService installmentService;

    @Test
    @SneakyThrows
    public void update_whenGivenRequestUpdateInstallment_thenReturnResponseUpdateInstallment() {
        RequestUpdateInstallment requestUpdateInstallment = new RequestUpdateInstallment();
        requestUpdateInstallment.setAmount(BigDecimal.TEN);
        requestUpdateInstallment.setInstallmentId(1L);
        String request = new ObjectMapper().writeValueAsString(requestUpdateInstallment);
        when(installmentService.update(any(RequestUpdateInstallment.class))).thenReturn(new ResponseUpdateInstallment());

        mockMvc.perform(put("/api/installment/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk());
        verify(installmentService, times(1)).update(any(RequestUpdateInstallment.class));
    }
}
