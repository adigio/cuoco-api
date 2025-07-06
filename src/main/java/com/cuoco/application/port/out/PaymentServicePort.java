package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.PaymentPreference;

public interface PaymentServicePort {
    PaymentPreference createPreference(Long userId, Integer planId);
} 