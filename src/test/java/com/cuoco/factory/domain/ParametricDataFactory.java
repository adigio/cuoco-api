package com.cuoco.factory.domain;

import com.cuoco.application.usecase.model.ParametricData;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.CookLevel;
import com.cuoco.application.usecase.model.Diet;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.MealType;
import com.cuoco.application.usecase.model.PreparationTime;
import com.cuoco.application.usecase.model.Unit;

import java.util.List;

public class ParametricDataFactory {

    public static ParametricData create() {
        return ParametricData.builder()
                .units(List.of(Unit.builder().id(1).description("Cup").symbol("cup").build()))
                .preparationTimes(List.of(PreparationTime.builder().id(1).description("15 minutes").build()))
                .cookLevels(List.of(CookLevel.builder().id(1).description("Easy").build()))
                .diets(List.of(Diet.builder().id(1).description("Vegetarian").build()))
                .mealTypes(List.of(MealType.builder().id(1).description("Lunch").build()))
                .allergies(List.of(Allergy.builder().id(1).description("Nuts").build()))
                .dietaryNeeds(List.of(DietaryNeed.builder().id(1).description("Gluten Free").build()))
                .build();
    }
} 