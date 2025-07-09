package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.PaymentResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public interface ProcessPaymentCallbackCommand {
    PaymentResult execute(Command command);

    @Getter
    @Builder
    @AllArgsConstructor
    class Command {
        private final String collectionId;
        private final String collectionStatus;
        private final String externalReference;
        private final String paymentType;
        private final String merchantOrderId;
        private final String preferenceId;
    }
} 