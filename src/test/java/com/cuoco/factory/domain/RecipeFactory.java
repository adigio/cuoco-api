package com.cuoco.factory.domain;

import com.cuoco.adapter.in.controller.model.IngredientRequest;
import com.cuoco.adapter.in.controller.model.RecipeRequest;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.CookLevel;
import com.cuoco.application.usecase.model.Diet;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.Filters;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.MealType;
import com.cuoco.application.usecase.model.ParametricData;
import com.cuoco.application.usecase.model.PreparationTime;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.RecipeConfiguration;
import com.cuoco.application.usecase.model.Step;
import com.cuoco.application.usecase.model.Unit;

import java.util.List;

public class RecipeFactory {

    public static Recipe create() {
        return Recipe.builder()
                .id(1L)
                .name("Test Recipe")
                .subtitle("Test Subtitle")
                .description("Test Description")
                .favorite(false)
                .steps(List.of(Step.builder()
                        .id(1L)
                        .title("Test Step")
                        .number(1)
                        .description("Test Step Description")
                        .time("10 minutes")
                        .build()))
                .image("test-image.jpg")
                .preparationTime(PreparationTime.builder().id(1).description("30 minutes").build())
                .cookLevel(CookLevel.builder().id(1).description("Beginner").build())
                .diet(Diet.builder().id(1).description("Vegetarian").build())
                .mealTypes(List.of(MealType.builder().id(1).description("Lunch").build()))
                .allergies(List.of(Allergy.builder().id(1).description("Nuts").build()))
                .dietaryNeeds(List.of(DietaryNeed.builder().id(1).description("Gluten Free").build()))
                .ingredients(List.of(Ingredient.builder()
                        .id(1L)
                        .name("Test Ingredient")
                        .quantity(1.0)
                        .unit(Unit.builder().id(1).description("Cup").symbol("cup").build())
                        .build()))
                .images(List.of(Step.builder()
                        .id(1L)
                        .title("Test Image")
                        .number(1)
                        .description("Test Image Description")
                        .time("5 minutes")
                        .build()))
                .filters(Filters.builder()
                        .useProfilePreferences(true)
                        .enable(true)
                        .preparationTime(PreparationTime.builder().id(1).description("30 minutes").build())
                        .cookLevel(CookLevel.builder().id(1).description("Beginner").build())
                        .diet(Diet.builder().id(1).description("Vegetarian").build())
                        .mealTypes(List.of(MealType.builder().id(1).description("Lunch").build()))
                        .allergies(List.of(Allergy.builder().id(1).description("Nuts").build()))
                        .dietaryNeeds(List.of(DietaryNeed.builder().id(1).description("Gluten Free").build()))
                        .freeze(false)
                        .build())
                .configuration(RecipeConfiguration.builder()
                        .size(4)
                        .notInclude(List.of())
                        .parametricData(ParametricData.builder().build())
                        .build())
                .build();
    }

    public static Recipe createWithName(String name) {
        return Recipe.builder()
                .id(1L)
                .name(name)
                .subtitle("Test Subtitle")
                .description("Test Description")
                .favorite(false)
                .steps(List.of(Step.builder()
                        .id(1L)
                        .title("Test Step")
                        .number(1)
                        .description("Test Step Description")
                        .time("10 minutes")
                        .build()))
                .image("test-image.jpg")
                .preparationTime(PreparationTime.builder().id(1).description("30 minutes").build())
                .cookLevel(CookLevel.builder().id(1).description("Beginner").build())
                .diet(Diet.builder().id(1).description("Vegetarian").build())
                .mealTypes(List.of(MealType.builder().id(1).description("Lunch").build()))
                .allergies(List.of(Allergy.builder().id(1).description("Nuts").build()))
                .dietaryNeeds(List.of(DietaryNeed.builder().id(1).description("Gluten Free").build()))
                .ingredients(List.of(Ingredient.builder()
                        .id(1L)
                        .name("Test Ingredient")
                        .quantity(1.0)
                        .unit(Unit.builder().id(1).description("Cup").symbol("cup").build())
                        .build()))
                .images(List.of(Step.builder()
                        .id(1L)
                        .title("Test Image")
                        .number(1)
                        .description("Test Image Description")
                        .time("5 minutes")
                        .build()))
                .filters(Filters.builder()
                        .useProfilePreferences(true)
                        .enable(true)
                        .preparationTime(PreparationTime.builder().id(1).description("30 minutes").build())
                        .cookLevel(CookLevel.builder().id(1).description("Beginner").build())
                        .diet(Diet.builder().id(1).description("Vegetarian").build())
                        .mealTypes(List.of(MealType.builder().id(1).description("Lunch").build()))
                        .allergies(List.of(Allergy.builder().id(1).description("Nuts").build()))
                        .dietaryNeeds(List.of(DietaryNeed.builder().id(1).description("Gluten Free").build()))
                        .freeze(false)
                        .build())
                .configuration(RecipeConfiguration.builder()
                        .size(4)
                        .notInclude(List.of())
                        .parametricData(ParametricData.builder().build())
                        .build())
                .build();
    }

    public static Recipe createWithFilters() {
        Recipe recipe = create();
        Filters filters = Filters.builder()
                .useProfilePreferences(true)
                .enable(true)
                .preparationTime(PreparationTime.builder().id(1).description("15 minutes").build())
                .cookLevel(CookLevel.builder().id(1).description("Easy").build())
                .diet(Diet.builder().id(1).description("Vegan").build())
                .mealTypes(List.of(MealType.builder().id(1).description("Breakfast").build()))
                .allergies(List.of(Allergy.builder().id(1).description("Dairy").build()))
                .dietaryNeeds(List.of(DietaryNeed.builder().id(1).description("Low Sodium").build()))
                .freeze(true)
                .build();
        recipe.setFilters(filters);
        return recipe;
    }

    public static Recipe createWithEmptyInstructions() {
        Recipe recipe = create();
        recipe.setSteps(List.of());
        return recipe;
    }

    public static Recipe createWithManySteps() {
        Recipe recipe = create();
        List<Step> manySteps = List.of(
                Step.builder().id(1L).title("Step 1").number(1).description("First step").time("5 minutes").build(),
                Step.builder().id(2L).title("Step 2").number(2).description("Second step").time("10 minutes").build(),
                Step.builder().id(3L).title("Step 3").number(3).description("Third step").time("15 minutes").build()
        );
        recipe.setSteps(manySteps);
        return recipe;
    }

    public static RecipeRequest getRecipeRequest() {
        Recipe recipe = create();
        return RecipeRequest.builder()
                .ingredients(recipe.getIngredients().stream().map(ingredient ->
                        IngredientRequest.builder()
                                .name(ingredient.getName())
                                .build())
                        .toList())
                .build();
    }
}
