package com.cuoco.adapter.out.hibernate.utils;

public enum Constants {

    INGREDIENT_NAMES("INGREDIENT_NAMES"),
    INGREDIENT_COUNT("INGREDIENT_COUNT"),
    COOK_LEVEL_ID("COOK_LEVEL_ID"),
    MAX_PREPARATION_TIME("MAX_PREPARATION_TIME"),
    MAX_RECIPES("MAX_RECIPES");

    private final String value;

    Constants(String value) { this.value = value; }

    public String getValue() {
        return value;
    }
}
