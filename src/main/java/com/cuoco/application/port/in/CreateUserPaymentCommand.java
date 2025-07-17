package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.UserPayment;

public interface CreateUserPaymentCommand {
    UserPayment execute();
}
