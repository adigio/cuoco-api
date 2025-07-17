package com.cuoco.shared.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentConstants {

    STATUS_PENDING(1),
    STATUS_APPROVED(2),
    STATUS_IN_PROCESS(3),
    STATUS_REJECTED(4);

    private final int value;

}