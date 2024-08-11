package com.colendi.demo.controller;

import com.colendi.demo.controller.request.RequestCreateCredit;
import com.colendi.demo.controller.response.ResponseCreateCredit;
import com.colendi.demo.service.ICreditService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CreditController.class)
@RunWith(SpringRunner.class)
public class CreditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICreditService creditService;

    @Test
    @SneakyThrows
    public void create_whenGivenRequestCreateCredit_thenReturnResponseCreateCredit() {
        RequestCreateCredit requestCreateCredit = new RequestCreateCredit();
        requestCreateCredit.setAmount(BigDecimal.TEN);
        requestCreateCredit.setUserId(1L);
        requestCreateCredit.setInstallmentCount(1);
        String request = new ObjectMapper().writeValueAsString(requestCreateCredit);
        when(creditService.create(any(RequestCreateCredit.class))).thenReturn(new ResponseCreateCredit());

        mockMvc.perform(post("/api/credit/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk());
        verify(creditService, times(1)).create(any(RequestCreateCredit.class));
    }

    @Test
    @SneakyThrows
    public void getByUserId_whenGivenValidRequest_thenReturnCreditPage() {
        when(creditService.getByUserId(
                any(Long.class),
                any(),
                any(),
                anyInt(), anyInt()
        )).thenReturn(Page.empty());

        mockMvc.perform(get("/api/credit/getByUserId?userId=1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        verify(creditService, times(1)).getByUserId(
                any(Long.class),
                any(),
                any(),
                anyInt(), anyInt()
        );
    }

}

