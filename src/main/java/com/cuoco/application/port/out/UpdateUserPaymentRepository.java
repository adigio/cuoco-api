package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.UserPayment;

public interface UpdateUserPaymentRepository {
    UserPayment execute(UserPayment userPayment);
}
