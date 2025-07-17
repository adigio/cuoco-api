package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.UserPayment;

public interface GetUserPaymentByExternalReferenceRepository {
    UserPayment execute(String externalReference);
}
