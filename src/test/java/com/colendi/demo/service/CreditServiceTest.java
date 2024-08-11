package com.colendi.demo.service;

import com.colendi.demo.controller.request.RequestCreateCredit;
import com.colendi.demo.controller.response.ResponseCreateCredit;
import com.colendi.demo.enums.CreditStatus;
import com.colendi.demo.mapper.CreditMapper;
import com.colendi.demo.model.Credit;
import com.colendi.demo.model.User;
import com.colendi.demo.repository.CreditRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreditServiceTest {

    @Mock
    private IUserService userService;

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private CreditMapper creditMapper;

    @InjectMocks
    private CreditService creditService;

    @Test
    public void create_whenGivenRequestCreateCredit_thenReturnResponseCreateCredit() {
        RequestCreateCredit request = new RequestCreateCredit();
        request.setUserId(1L);
        request.setAmount(BigDecimal.valueOf(1000));
        request.setInstallmentCount(5);

        User user = new User();
        Credit credit = new Credit();
        credit.setId(1L);

        when(userService.findById(request.getUserId())).thenReturn(user);
        when(creditRepository.save(any(Credit.class))).thenReturn(credit);
        when(creditMapper.toResponseCreateCredit(credit)).thenReturn(new ResponseCreateCredit());

        ResponseCreateCredit response = creditService.create(request);

        assertNotNull(response);
        verify(userService).findById(request.getUserId());
        verify(creditRepository).save(any(Credit.class));
        verify(creditMapper).toResponseCreateCredit(any(Credit.class));
    }

    @Test
    public void getByUserId_whenGivenValidRequest_thenReturnCreditPage() {
        Long userId = 1L;
        CreditStatus status = CreditStatus.OPEN;
        LocalDateTime createDate = LocalDateTime.now();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Credit> page = mock(Page.class);

        when(creditRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        Page<Credit> result = creditService.getByUserId(userId, status, createDate, 0, 10);

        assertNotNull(result);
        verify(creditRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    public void save_whenGivenValidRequest_thenReturnVoid() {
        Credit credit = new Credit();
        creditService.save(credit);
        verify(creditRepository).save(credit);
    }

    @Test
    public void save_whenGivenNull_thenReturnVoid() {
        creditService.save(null);
        verify(creditRepository, never()).save(any(Credit.class));
    }
}
