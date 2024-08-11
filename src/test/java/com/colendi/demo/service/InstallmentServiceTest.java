package com.colendi.demo.service;

import com.colendi.demo.controller.request.RequestUpdateInstallment;
import com.colendi.demo.controller.response.ResponseUpdateInstallment;
import com.colendi.demo.enums.InstallmentStatus;
import com.colendi.demo.exception.MicroException;
import com.colendi.demo.mapper.InstallmentMapper;
import com.colendi.demo.model.Credit;
import com.colendi.demo.model.Installment;
import com.colendi.demo.repository.InstallmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InstallmentServiceTest {

    @Mock
    private InstallmentRepository installmentRepository;

    @Mock
    private InstallmentMapper installmentMapper;

    @Mock
    private CreditService creditService;

    @InjectMocks
    private InstallmentService installmentService;


    @Test
    public void update_whenGivenRequestUpdateInstallment_thenReturnResponseUpdateInstallment() {
        RequestUpdateInstallment request = new RequestUpdateInstallment();
        request.setInstallmentId(1L);
        request.setAmount(new BigDecimal("50.00"));

        Installment installment = new Installment();
        installment.setInstallmentStatus(InstallmentStatus.OPEN);
        installment.setAmount(new BigDecimal("100.00"));

        Credit credit = new Credit();
        credit.setInstallments(List.of(installment));

        Installment updatedInstallment = new Installment();
        updatedInstallment.setInstallmentStatus(InstallmentStatus.CLOSED);
        updatedInstallment.setAmount(BigDecimal.ZERO);
        updatedInstallment.setCredit(credit);

        ResponseUpdateInstallment response = new ResponseUpdateInstallment();

        when(installmentRepository.findById(request.getInstallmentId())).thenReturn(Optional.of(installment));
        when(installmentRepository.save(any(Installment.class))).thenReturn(updatedInstallment);
        doNothing().when(creditService).save(any(Credit.class));
        when(installmentMapper.toResponseUpdateInstallment(updatedInstallment)).thenReturn(response);

        ResponseUpdateInstallment result = installmentService.update(request);

        verify(installmentRepository).save(any(Installment.class));
        verify(creditService).save(updatedInstallment.getCredit());
        verify(installmentMapper).toResponseUpdateInstallment(updatedInstallment);
        assertEquals(response, result);
    }

    @Test
    public void update_whenGivenAmountSame_thenReturnResponseUpdateInstallment() {
        RequestUpdateInstallment request = new RequestUpdateInstallment();
        request.setInstallmentId(1L);
        request.setAmount(new BigDecimal("100.00"));

        Installment installment = new Installment();
        installment.setInstallmentStatus(InstallmentStatus.OPEN);
        installment.setAmount(new BigDecimal("100.00"));

        Credit credit = new Credit();
        credit.setInstallments(List.of(installment));

        Installment updatedInstallment = new Installment();
        updatedInstallment.setInstallmentStatus(InstallmentStatus.CLOSED);
        updatedInstallment.setAmount(BigDecimal.ZERO);
        updatedInstallment.setCredit(credit);

        ResponseUpdateInstallment response = new ResponseUpdateInstallment();

        when(installmentRepository.findById(request.getInstallmentId())).thenReturn(Optional.of(installment));
        when(installmentRepository.save(any(Installment.class))).thenReturn(updatedInstallment);
        doNothing().when(creditService).save(any(Credit.class));
        when(installmentMapper.toResponseUpdateInstallment(updatedInstallment)).thenReturn(response);

        ResponseUpdateInstallment result = installmentService.update(request);

        verify(installmentRepository).save(any(Installment.class));
        verify(creditService).save(updatedInstallment.getCredit());
        verify(installmentMapper).toResponseUpdateInstallment(updatedInstallment);
        assertEquals(response, result);
    }

    @Test
    public void update_whenGivenAmountSame_thenThrowInstallmentPaidBefore() {
        RequestUpdateInstallment request = new RequestUpdateInstallment();
        request.setInstallmentId(1L);

        Installment installment = new Installment();
        installment.setInstallmentStatus(InstallmentStatus.CLOSED);

        when(installmentRepository.findById(request.getInstallmentId())).thenReturn(Optional.of(installment));
        assertThrows(MicroException.class, () -> installmentService.update(request));
    }

    @Test
    public void update_whenGivenRequestUpdateInstallment_thenThrowInstallmentNotFound() {
        RequestUpdateInstallment request = new RequestUpdateInstallment();
        request.setInstallmentId(1L);

        when(installmentRepository.findById(request.getInstallmentId())).thenReturn(Optional.empty());

        assertThrows(MicroException.class, () -> installmentService.update(request));
    }

    @Test
    public void addPenalty_whenGivenNothing_thenReturnVoid() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Installment installment = new Installment();
        installment.setInstallmentStatus(InstallmentStatus.OPEN);
        installment.setAmount(new BigDecimal("100.00"));
        installment.setDueDate(Timestamp.valueOf(LocalDateTime.now().minusDays(10)));

        Credit credit = new Credit();
        credit.setAmount(new BigDecimal("1000.00"));
        credit.setInstallments(List.of(installment));
        installment.setCredit(credit);

        List<Installment> installments = List.of(installment);

        when(installmentRepository.findByInstallmentStatusAndDueDateBefore(any(InstallmentStatus.class), any(Timestamp.class))).thenReturn(installments);
        when(installmentRepository.save(any(Installment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(creditService).save(any(Credit.class));

        installmentService.addPenalty();

        verify(installmentRepository).save(any(Installment.class));
        verify(creditService).save(any(Credit.class));
    }
}
