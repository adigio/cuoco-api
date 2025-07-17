package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.PaymentStatus;

import java.util.List;

public interface GetAllPaymentStatusRepository {
    List<PaymentStatus> getAll();
}
