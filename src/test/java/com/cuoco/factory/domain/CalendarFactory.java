package com.cuoco.factory.domain;

import com.cuoco.application.usecase.model.Calendar;
import com.cuoco.application.usecase.model.CalendarRecipe;
import com.cuoco.application.usecase.model.Day;
import com.cuoco.application.usecase.model.MealType;
import com.cuoco.application.usecase.model.Recipe;

import java.util.List;

public class CalendarFactory {

    public static Calendar create() {
        return Calendar.builder()
                .day(Day.builder()
                        .id(1)
                        .description("Monday")
                        .build())
                .recipes(List.of(CalendarRecipe.builder()
                        .recipe(RecipeFactory.create())
                        .mealType(MealType.builder()
                                .id(1)
                                .description("Lunch")
                                .build())
                        .build()))
                .build();
    }
} 