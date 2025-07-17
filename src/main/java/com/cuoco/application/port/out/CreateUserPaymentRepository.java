package com.cuoco.application.port.out;


import com.cuoco.application.usecase.model.UserPayment;

public interface CreateUserPaymentRepository {
    UserPayment execute(UserPayment userPayment);
}
