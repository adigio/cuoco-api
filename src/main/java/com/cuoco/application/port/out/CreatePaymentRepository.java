package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.UserPayment;

public interface CreatePaymentRepository {
    UserPayment execute(Long userId, Integer planId);
} 