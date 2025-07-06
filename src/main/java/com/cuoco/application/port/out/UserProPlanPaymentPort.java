package com.cuoco.application.port.out;

import com.cuoco.adapter.out.hibernate.model.UserProPlanPaymentHibernateModel;
import com.cuoco.application.usecase.model.PaymentStatus;

import java.util.Optional;

public interface UserProPlanPaymentPort {
    void saveProPlanPayment(Long userId, String externalReference, PaymentStatus status);
    boolean isUserPro(Long userId);
    Optional<UserProPlanPaymentHibernateModel> findByExternalReference(String externalReference);
} 