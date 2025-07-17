package com.cuoco.shared.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlanConstants {

    FREE(1),
    PRO(2);

    private final int value;

}
