package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.UserPayment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public interface CreateUserPaymentCommand {
    UserPayment execute(Command command);

    @Data
    @Builder
    @AllArgsConstructor
    class Command {
        private final Integer planId;
    }
} 