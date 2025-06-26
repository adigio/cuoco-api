package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.IngredientResponse;
import com.cuoco.adapter.in.controller.model.ParametricResponse;
import com.cuoco.adapter.in.controller.model.QuickRecipeRequest;
import com.cuoco.adapter.in.controller.model.RecipeResponse;
import com.cuoco.adapter.in.controller.model.UnitResponse;
import com.cuoco.application.port.in.FindOrGenerateRecipeCommand;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.CookLevel;
import com.cuoco.application.usecase.model.Diet;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.MealType;
import com.cuoco.application.usecase.model.PreparationTime;
import com.cuoco.application.usecase.model.Recipe;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/quick-recipes")
public class QuickRecipeControllerAdapter {

    private final FindOrGenerateRecipeCommand findOrGenerateRecipeCommand;

    public QuickRecipeControllerAdapter(FindOrGenerateRecipeCommand findOrGenerateRecipeCommand) {
        this.findOrGenerateRecipeCommand = findOrGenerateRecipeCommand;
    }

    @PostMapping()
    public ResponseEntity<RecipeResponse> findOrGenerate(@Valid @RequestBody QuickRecipeRequest request) {
        
        log.info("Executing find or generate recipe with name: {}", request.getRecipeName());

        Recipe recipe = findOrGenerateRecipeCommand.execute(buildCommand(request));

        RecipeResponse response = buildResponse(recipe);

        log.info("Successfully found or generated recipe: {}", recipe.getName());
        return ResponseEntity.ok(response);
    }

    private FindOrGenerateRecipeCommand.Command buildCommand(QuickRecipeRequest request) {
        return FindOrGenerateRecipeCommand.Command.builder()
                .recipeName(request.getRecipeName())
                .build();
    }

    private RecipeResponse buildResponse(Recipe recipe) {
        return RecipeResponse.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .subtitle(recipe.getSubtitle())
                .description(recipe.getDescription())
                .instructions(recipe.getInstructions())
                .image(recipe.getImage())
                .preparationTime(buildParametricResponse(recipe.getPreparationTime()))
                .cookLevel(buildParametricResponse(recipe.getCookLevel()))
                .diet(buildParametricResponse(recipe.getDiet()))
                .mealTypes(recipe.getMealTypes().stream().map(this::buildParametricResponse).toList())
                .allergies(recipe.getAllergies().stream().map(this::buildParametricResponse).toList())
                .dietaryNeeds(recipe.getDietaryNeeds().stream().map(this::buildParametricResponse).toList())
                .ingredients(recipe.getIngredients().stream().map(this::buildIngredientResponse).toList())
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

    private ParametricResponse buildParametricResponse(PreparationTime preparationTime) {
        return ParametricResponse.builder()
                .id(preparationTime.getId())
                .description(preparationTime.getDescription())
                .build();
    }

    private ParametricResponse buildParametricResponse(CookLevel cookLevel) {
        return ParametricResponse.builder()
                .id(cookLevel.getId())
                .description(cookLevel.getDescription())
                .build();
    }

    private ParametricResponse buildParametricResponse(Diet diet) {
        return ParametricResponse.builder()
                .id(diet.getId())
                .description(diet.getDescription())
                .build();
    }

    private ParametricResponse buildParametricResponse(MealType mealType) {
        return ParametricResponse.builder()
                .id(mealType.getId())
                .description(mealType.getDescription())
                .build();
    }

    private ParametricResponse buildParametricResponse(Allergy allergy) {
        return ParametricResponse.builder()
                .id(allergy.getId())
                .description(allergy.getDescription())
                .build();
    }

    private ParametricResponse buildParametricResponse(DietaryNeed dietaryNeed) {
        return ParametricResponse.builder()
                .id(dietaryNeed.getId())
                .description(dietaryNeed.getDescription())
                .build();
    }
} 