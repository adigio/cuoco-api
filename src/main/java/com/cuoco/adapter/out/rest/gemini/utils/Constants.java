package com.cuoco.adapter.out.rest.gemini.utils;

public enum Constants {

    INGREDIENTS("INGREDIENTS"),
    MAX_RECIPES("MAX_RECIPES"),

    PREPARATION_TIME("COOK_TIME"),
    COOK_LEVEL("COOK_LEVEL"),
    MEAL_TYPES("MEAL_TYPES"),
    MEAL_CATEGORIES("MEAL_CATEGORIES");

    private final String value;

    Constants(String value) { this.value = value; }

    public String getValue() {

        String WILDCARD_START = "{{";
        String WILDCARD_END = "}}";

        return WILDCARD_START + value + WILDCARD_END;
    }
}
