package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.UserProPlanPaymentHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.UserProPlanPaymentRepository;
import com.cuoco.application.port.out.UserProPlanPaymentPort;
import com.cuoco.application.usecase.model.PaymentStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class UserProPlanPaymentAdapter implements UserProPlanPaymentPort {
    private final UserProPlanPaymentRepository repository;

    public UserProPlanPaymentAdapter(UserProPlanPaymentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveProPlanPayment(Long userId, String externalReference, PaymentStatus status) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiration = now.plusMonths(1);
        UserProPlanPaymentHibernateModel entity = UserProPlanPaymentHibernateModel.builder()
                .userId(userId)
                .externalReference(externalReference)
                .startDate(now)
                .expirationDate(expiration)
                .paymentStatus(status.name())
                .build();
        repository.save(entity);
    }

    @Override
    public boolean isUserPro(Long userId) {
        return repository.findTopByUserIdAndExpirationDateAfterOrderByExpirationDateDesc(userId, LocalDateTime.now())
                .filter(e -> e.getPaymentStatus().equals(PaymentStatus.APPROVED.name()))
                .isPresent();
    }

    @Override
    public Optional<UserProPlanPaymentHibernateModel> findByExternalReference(String externalReference) {
        return repository.findByExternalReference(externalReference);
    }
} 