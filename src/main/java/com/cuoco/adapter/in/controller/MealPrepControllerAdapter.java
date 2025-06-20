package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.*;
import com.cuoco.application.port.in.GetMealPrepFromIngredientsCommand;
import com.cuoco.application.port.in.GetRecipesFromIngredientsCommand;
import com.cuoco.application.usecase.model.CookLevel;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.MealPrep;
import com.cuoco.application.usecase.model.RecipeFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/meal-preps")
public class MealPrepControllerAdapter {

    private final GetMealPrepFromIngredientsCommand getMealPrepFromIngredientsCommand;

    public MealPrepControllerAdapter(GetMealPrepFromIngredientsCommand getMealPrepFromIngredientsCommand) {
        this.getMealPrepFromIngredientsCommand = getMealPrepFromIngredientsCommand;
    }

    @PostMapping
    public ResponseEntity<List<MealPrepResponse>> generate(@RequestBody RecipeRequest mealPrepRequest){

        log.info("Executing GET mealPrep from ingredients with body {}", mealPrepRequest);

        List<MealPrep> mealPreps = getMealPrepFromIngredientsCommand.execute(buildGenerateMealPrepCommand(mealPrepRequest));

        List<MealPrepResponse> mealPrepsResponse = mealPreps.stream().map(this::builResponse).toList();

        log.info("Successfully generated recipes");
        return ResponseEntity.ok(mealPrepsResponse);
    }

    private GetMealPrepFromIngredientsCommand.Command buildGenerateMealPrepCommand(RecipeRequest mealPrepRequest) {
        return GetMealPrepFromIngredientsCommand.Command.builder()
                .filters(mealPrepRequest.getFilters() != null ? buildFilter(mealPrepRequest.getFilters()) : null)
                .ingredients(mealPrepRequest.getIngredients().stream().map(this::buildIngredient).toList())
                .build();
    }

    private RecipeFilter buildFilter(RecipeFilterRequest filter) {
        return RecipeFilter.builder()
                .time(filter.getTime())
                .difficulty(
                        CookLevel.builder()
                                .description(filter.getDifficulty())
                                .build()
                )
                .types(filter.getTypes())
                .diet(filter.getDiet())
                .quantity(filter.getQuantity())
                .build();
    }

    private Ingredient buildIngredient(IngredientRequest ingredientRequest) {
        return Ingredient.builder()
                .name(ingredientRequest.getName())
                .source(ingredientRequest.getSource())
                .confirmed(ingredientRequest.isConfirmed())
                .build();
    }

    private MealPrepResponse builResponse(MealPrep mealPrep) {
        return MealPrepResponse.builder()
                .id(mealPrep.getId())
                .name(mealPrep.getName())
                .subtitle(mealPrep.getSubtitle())
                .description(mealPrep.getDescription())
                .preparationTime(mealPrep.getPreparationTime())
                .instructions(mealPrep.getInstructions())
                .ingredients(
                        mealPrep.getIngredients().stream().map(this::buildIngredientResponse).toList()
                )
                .cookLevel(
                        ParametricResponse.builder()
                                .id(mealPrep.getCookLevel().getId())
                                .description(mealPrep.getCookLevel().getDescription())
                                .build()
                )
                .build();
    }

    private IngredientResponse buildIngredientResponse(Ingredient ingredient) {
        return IngredientResponse.builder()
                .name(ingredient.getName())
                .quantity(ingredient.getQuantity())
                .unit(UnitResponse.builder()
                        .id(ingredient.getUnit().getId())
                        .description(ingredient.getUnit().getDescription())
                        .symbol(ingredient.getUnit().getSymbol())
                        .build()
                )
                .build();
    }
}
