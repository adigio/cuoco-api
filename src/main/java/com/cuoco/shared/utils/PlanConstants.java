package com.cuoco.shared.utils;

public enum PlanConstants {

    FREE(1),
    PREMIUM(2);

    private final int value;

    PlanConstants(int value) { this.value = value; }

    public int getValue() {
        return value;
    }
}
