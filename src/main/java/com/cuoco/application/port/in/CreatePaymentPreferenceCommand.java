package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.PaymentPreference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public interface CreatePaymentPreferenceCommand {
    PaymentPreference execute(Command command);

    @Getter
    @Builder
    @AllArgsConstructor
    class Command {
        private final Long userId;
        private final Integer planId;
    }
} 