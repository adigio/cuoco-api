package com.cuoco.adapter.out.rest.gemini.utils;

public enum Constants {

    // Recipe creation placeholders
    INGREDIENTS("INGREDIENTS"),
    MAX_RECIPES("MAX_RECIPES"),
    MAX_STEP_IMAGES("MAX_STEP_IMAGES"),
    MAX_MEAL_PREPS("MAX_MEAL_PREPS"),

    // Image creation placeholders
    MAIN_INGREDIENTS("MAIN_INGREDIENTS"),
    STEP_NUMBER("STEP_NUMBER"),
    STEP_INSTRUCTION("STEP_INSTRUCTION"),
    RECIPE_NAME("RECIPE_NAME"),

    // Data for Gemini to create recipes
    PARAMETRIC_UNITS("PARAMETRIC_UNITS"),
    PARAMETRIC_PREPARATION_TIMES("PARAMETRIC_PREPARATION_TIMES"),
    PARAMETRIC_COOK_LEVELS("PARAMETRIC_COOK_LEVELS"),
    PARAMETRIC_DIETS("PARAMETRIC_DIETS"),
    PARAMETRIC_MEAL_TYPES("PARAMETRIC_MEAL_TYPES"),
    PARAMETRIC_ALLERGIES("PARAMETRIC_ALLERGIES"),
    PARAMETRIC_DIETARY_NEEDS("PARAMETRIC_DIETARY_NEEDS"),

    // Filters placeholders
    PREPARATION_TIME("COOK_TIME"),
    COOK_LEVEL("COOK_LEVEL"),
    DIET("DIET"),
    MEAL_TYPES("MEAL_TYPES"),
    ALLERGIES("ALLERGIES"),
    DIETARY_NEEDS("DIETARY_NEEDS"),
    QUANTITY("QUANTITY"),
    FREEZE("FREEZE");

    private final String value;

    Constants(String value) { this.value = value; }

    public String getValue() {

        String WILDCARD_START = "{{";
        String WILDCARD_END = "}}";

        return WILDCARD_START + value + WILDCARD_END;
    }
}
