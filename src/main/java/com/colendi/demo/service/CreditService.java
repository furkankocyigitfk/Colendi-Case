package com.colendi.demo.service;

import com.colendi.demo.controller.request.RequestCreateCredit;
import com.colendi.demo.controller.response.ResponseCreateCredit;
import com.colendi.demo.enums.CreditStatus;
import com.colendi.demo.enums.InstallmentStatus;
import com.colendi.demo.mapper.CreditMapper;
import com.colendi.demo.model.Credit;
import com.colendi.demo.model.Installment;
import com.colendi.demo.model.User;
import com.colendi.demo.repository.CreditRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CreditService implements ICreditService {
    private final IUserService userService;
    private final CreditRepository creditRepository;
    private final CreditMapper creditMapper;

    @Override
    public ResponseCreateCredit create(RequestCreateCredit request) {
        User user = userService.findById(request.getUserId());
        Credit credit = creditRepository.save(getCredit(request, user));

        return creditMapper.toResponseCreateCredit(credit);
    }

    @Override
    public Page<Credit> getByUserId(Long userId, CreditStatus status, LocalDateTime createDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        return creditRepository.findAll((Root<Credit> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            var predicates = new ArrayList<Predicate>();

            predicates.add(cb.equal(root.get("user").get("id"), userId));

            if (createDate != null) {
                predicates.add(cb.equal(root.get("createDate"), createDate));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("creditStatus"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }

    @Override
    public void save(Credit credit) {
        if (Objects.isNull(credit)) {
            return;
        }
        creditRepository.save(credit);
    }

    private static Credit getCredit(RequestCreateCredit request, User user) {
        Credit credit = new Credit();
        credit.setUser(user);
        credit.setCreditStatus(CreditStatus.OPEN);
        credit.setAmount(request.getAmount());

        List<Installment> installmentList = getInstallments(request, credit);
        credit.setInstallments(installmentList);
        return credit;
    }

    private static List<Installment> getInstallments(RequestCreateCredit request, Credit credit) {
        List<Installment> installmentList = new ArrayList<>();
        BigDecimal partialAmount = request.getAmount().divide(BigDecimal.valueOf(request.getInstallmentCount()), 2, RoundingMode.HALF_UP);
        Instant instant = Instant.now();
        for (int i = 0; i < request.getInstallmentCount(); i++) {
            instant = instant.plus(30, ChronoUnit.DAYS);
            Installment installment = new Installment();
            installment.setAmount(partialAmount);
            installment.setDueDate(Timestamp.from(instant));
            installment.setInstallmentStatus(InstallmentStatus.OPEN);
            installment.setCredit(credit);
            installmentList.add(installment);
        }
        return installmentList;
    }
}
