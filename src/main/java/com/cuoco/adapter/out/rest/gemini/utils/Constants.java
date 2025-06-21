package com.cuoco.adapter.out.rest.gemini.utils;

public enum Constants {

    INGREDIENTS("INGREDIENTS"),
    MAX_RECIPES("MAX_RECIPES"),
    COOK_TIME("COOK_TIME"),
    COOK_LEVEL("COOK_LEVEL"),
    FOOD_TYPES("FOOD_TYPES"),
    QUANTITY("QUANTITY"),
    DIET("DIET"),
    FREEZE("FREEZE");

    private final String value;

    Constants(String value) { this.value = value; }

    public String getValue() {

        String WILDCARD_START = "{{";
        String WILDCARD_END = "}}";

        return WILDCARD_START + value + WILDCARD_END;
    }
}
