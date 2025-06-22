package com.cuoco.shared.utils;

public enum Constants {

    COMMA(","),
    SLASH("/"),
    DOT("."),
    EMPTY("");

    private final String value;

    Constants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}