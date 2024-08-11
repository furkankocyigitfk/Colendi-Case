package com.colendi.demo.service;

import com.colendi.demo.controller.request.RequestUpdateInstallment;
import com.colendi.demo.controller.response.ResponseUpdateInstallment;
import com.colendi.demo.enums.CreditStatus;
import com.colendi.demo.enums.InstallmentStatus;
import com.colendi.demo.exception.MicroException;
import com.colendi.demo.mapper.InstallmentMapper;
import com.colendi.demo.model.Installment;
import com.colendi.demo.repository.InstallmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InstallmentService implements IInstallmentService {
    private final InstallmentRepository installmentRepository;
    private final InstallmentMapper installmentMapper;
    private final CreditService creditService;
    private static final BigDecimal INTEREST_RATE = BigDecimal.TEN;

    @Override
    public ResponseUpdateInstallment update(RequestUpdateInstallment request) {
        Optional<Installment> optionalInstallment = installmentRepository.findById(request.getInstallmentId());

        if (optionalInstallment.isEmpty()) {
            throw new MicroException("Installement not found");
        }
        Installment installment = getInstallment(request, optionalInstallment.get());
        Installment savedInstallment = installmentRepository.save(installment);
        List<Installment> nonPaidInstallments = savedInstallment.getCredit().getInstallments().stream().filter(x -> !BigDecimal.ZERO.equals(x.getAmount())).toList();
        if (!CollectionUtils.isEmpty(nonPaidInstallments)) {
            savedInstallment.getCredit().setCreditStatus(CreditStatus.CLOSED);
        }

        creditService.save(savedInstallment.getCredit());

        return installmentMapper.toResponseUpdateInstallment(savedInstallment);
    }

    // @Scheduled(cron = "0 0 0 * * ?")
    @Scheduled(cron = "0 */1 * * * ?")
    @Transactional
    public void updateStatus() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        List<Installment> installments = installmentRepository.findByInstallmentStatusAndDueDateBefore(InstallmentStatus.OPEN, now);
        for (Installment installment : installments) {
            BigDecimal delayedAmount = getDelayedAmount(installment, now);
            installment.setInstallmentStatus(InstallmentStatus.DELAYED);
            installment.setAmount(installment.getAmount().add(delayedAmount));
            Installment savedInstallment = installmentRepository.save(installment);

            savedInstallment.getCredit().setAmount(savedInstallment.getCredit().getAmount().add(delayedAmount));
            creditService.save(savedInstallment.getCredit());
        }
    }

    private static BigDecimal getDelayedAmount(Installment installment, Timestamp now) {
        // (Gecikmeye Düşen Gün Sayısı * (Faiz Oranı / 100 ) * Tutar ) / 360
        LocalDate timestampDate = installment.getDueDate().toLocalDateTime().toLocalDate();
        LocalDate nowDate = now.toLocalDateTime().toLocalDate();

        long daysBetween = ChronoUnit.DAYS.between(timestampDate, nowDate);
        return (INTEREST_RATE.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(daysBetween)).multiply(installment.getAmount()).divide(BigDecimal.valueOf(360), 2, RoundingMode.HALF_UP);
    }

    private static Installment getInstallment(RequestUpdateInstallment request, Installment installment) {
        if (!InstallmentStatus.OPEN.equals(installment.getInstallmentStatus())) {
            throw new MicroException("Installement has already paid before");
        }
        if (installment.getAmount().compareTo(request.getAmount()) <= 0) {
            installment.setAmount(BigDecimal.ZERO);
            installment.setInstallmentStatus(InstallmentStatus.CLOSED);
        } else {
            installment.setAmount(installment.getAmount().subtract(request.getAmount()));
        }
        return installment;
    }
}
