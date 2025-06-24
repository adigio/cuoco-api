package com.cuoco.application.usecase.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ParametricData {

    private List<Unit> units;
    private List<PreparationTime> preparationTimes;
    private List<CookLevel> cookLevels;
    private List<Diet> diets;
    private List<MealType> mealTypes;
    private List<Allergy> allergies;
    private List<DietaryNeed> dietaryNeeds;

}
