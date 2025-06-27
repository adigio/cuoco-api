package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.IngredientRequest;
import com.cuoco.adapter.in.controller.model.IngredientResponse;
import com.cuoco.adapter.in.controller.model.ParametricResponse;
import com.cuoco.adapter.in.controller.model.RecipeConfiguration;
import com.cuoco.adapter.in.controller.model.RecipeFilterRequest;
import com.cuoco.adapter.in.controller.model.RecipeImageResponse;
import com.cuoco.adapter.in.controller.model.RecipeRequest;
import com.cuoco.adapter.in.controller.model.RecipeResponse;
import com.cuoco.adapter.in.controller.model.UnitResponse;
import com.cuoco.application.port.in.GetRecipesFromIngredientsCommand;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.CookLevel;
import com.cuoco.application.usecase.model.Diet;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.MealType;
import com.cuoco.application.usecase.model.PreparationTime;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.RecipeImage;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/recipes")
@Tag(name = "Recipes", description = "Obtains recipes from ingredients, filters and configuration")
public class RecipeControllerAdapter {

    private final GetRecipesFromIngredientsCommand getRecipesFromIngredientsCommand;

    public RecipeControllerAdapter(GetRecipesFromIngredientsCommand getRecipesFromIngredientsCommand) {
        this.getRecipesFromIngredientsCommand = getRecipesFromIngredientsCommand;
    }

    @PostMapping()
    public ResponseEntity<List<RecipeResponse>> generate(@RequestBody @Valid RecipeRequest recipeRequest) {

        log.info("Executing GET recipes from ingredients with body {}", recipeRequest);

        List<Recipe> recipes = getRecipesFromIngredientsCommand.execute(buildGenerateRecipeCommand(recipeRequest));

        List<RecipeResponse> recipesResponse = recipes.stream().map(this::buildResponse).toList();

        log.info("Successfully generated recipes");
        return ResponseEntity.ok(recipesResponse);
    }

    private GetRecipesFromIngredientsCommand.Command buildGenerateRecipeCommand(RecipeRequest recipeRequest) {

        boolean filtersEnabled = true;

        if(recipeRequest.getFilters() == null) {
            filtersEnabled = false;
            recipeRequest.setFilters(new RecipeFilterRequest());
        }

        if(recipeRequest.getConfiguration() == null) recipeRequest.setConfiguration(new RecipeConfiguration());

        return GetRecipesFromIngredientsCommand.Command.builder()
                .filtersEnabled(filtersEnabled)
                .ingredients(recipeRequest.getIngredients().stream().map(this::buildIngredient).toList())
                .preparationTimeId(recipeRequest.getFilters().getPreparationTimeId())
                .servings(recipeRequest.getFilters().getServings())
                .cookLevelId(recipeRequest.getFilters().getCookLevelId())
                .dietId(recipeRequest.getFilters().getDietId())
                .typeIds(recipeRequest.getFilters().getTypeIds())
                .allergiesIds(recipeRequest.getFilters().getAllergiesIds())
                .dietaryNeedsIds(recipeRequest.getFilters().getDietaryNeedsIds())
                .size(recipeRequest.getConfiguration().getSize())
                .notInclude(recipeRequest.getConfiguration().getNotInclude())
                .build();
    }

    private Ingredient buildIngredient(IngredientRequest ingredientRequest) {
        return Ingredient.builder()
                .name(ingredientRequest.getName())
                .build();
    }

    private RecipeResponse buildResponse(Recipe recipe) {

        ParametricResponse diet = recipe.getDiet() != null ? buildParametricResponse(recipe.getDiet()) : null;

        List<ParametricResponse> allergies = Optional.ofNullable(recipe.getAllergies()).orElse(Collections.emptyList())
                .stream()
                .map(this::buildParametricResponse)
                .toList();

        List<ParametricResponse> dietaryNeeds = Optional.ofNullable(recipe.getDietaryNeeds()).orElse(Collections.emptyList())
                .stream()
                .map(this::buildParametricResponse)
                .toList();

        return RecipeResponse.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .subtitle(recipe.getSubtitle())
                .description(recipe.getDescription())
                .instructions(recipe.getInstructions())
                .image(recipe.getImage())
                .preparationTime(buildParametricResponse(recipe.getPreparationTime()))
                .cookLevel(buildParametricResponse(recipe.getCookLevel()))
                .diet(diet)
                .mealTypes(recipe.getMealTypes().stream().map(this::buildParametricResponse).toList())
                .allergies(allergies)
                .dietaryNeeds(dietaryNeeds)
                .ingredients(recipe.getIngredients().stream().map(this::buildIngredientResponse).toList())
                .build();
    }

    private IngredientResponse buildIngredientResponse(Ingredient ingredient) {
        return IngredientResponse.builder()
                .id(ingredient.getId())
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

    private RecipeImageResponse buildRecipeImageResponse(RecipeImage recipeImage) {
        return RecipeImageResponse.builder()
                .id(recipeImage.getId())
                .imageName(recipeImage.getImageName())
                .imageType(recipeImage.getImageType())
                .stepNumber(recipeImage.getStepNumber())
                .stepDescription(recipeImage.getStepDescription())
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