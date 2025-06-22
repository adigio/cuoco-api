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
import com.cuoco.application.port.in.GenerateRecipeImagesCommand;
import com.cuoco.application.port.in.GetRecipesFromIngredientsCommand;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.MealCategory;
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

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/recipes")
public class RecipeControllerAdapter {

    private final GetRecipesFromIngredientsCommand getRecipesFromIngredientsCommand;
    private final GenerateRecipeImagesCommand generateRecipeImagesCommand;

    public RecipeControllerAdapter(
            GetRecipesFromIngredientsCommand getRecipesFromIngredientsCommand,
            GenerateRecipeImagesCommand generateRecipeImagesCommand
    ) {
        this.getRecipesFromIngredientsCommand = getRecipesFromIngredientsCommand;
        this.generateRecipeImagesCommand = generateRecipeImagesCommand;
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

        Boolean filtersEnabled = true;

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
                .typeIds(recipeRequest.getFilters().getTypeIds())
                .categoryIds(recipeRequest.getFilters().getCategoryIds())
                .recipesSize(recipeRequest.getConfiguration().getRecipesSize())
                .notInclude(recipeRequest.getConfiguration().getNotInclude())
                .build();
    }

    private Ingredient buildIngredient(IngredientRequest ingredientRequest) {
        return Ingredient.builder()
                .name(ingredientRequest.getName())
                .build();
    }

    private RecipeResponse buildResponse(Recipe recipe) {
        return RecipeResponse.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .subtitle(recipe.getSubtitle())
                .description(recipe.getDescription())
                .image(recipe.getImage())
                .instructions(recipe.getInstructions())
                .preparationTime(buildParametricResponse(recipe.getPreparationTime()))
                .type(buildParametricResponse(recipe.getType()))
                .categories(recipe.getCategories().stream().map(this::buildParametricResponse).toList())
                .ingredients(recipe.getIngredients().stream().map(this::buildIngredientResponse).toList())
                .cookLevel(
                        ParametricResponse.builder()
                                .id(recipe.getCookLevel().getId())
                                .description(recipe.getCookLevel().getDescription())
                                .build()
                )
                .generatedImages(buildImages(recipe))
                .build();
    }

    private List<RecipeImageResponse> buildImages(Recipe recipe) {
        try {
            if (generateRecipeImagesCommand != null) {
                return generateRecipeImagesCommand.execute(GenerateRecipeImagesCommand.Command.builder().recipe(recipe).build())
                        .stream().map(RecipeImageResponse::fromDomain).toList();
            }
            return List.of();
        } catch (Exception e) {
            log.warn("Failed to generate images for recipe: {}", recipe.getName(), e);
            return List.of();
        }
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

    private ParametricResponse buildParametricResponse(MealType mealType) {
        return ParametricResponse.builder()
                .id(mealType.getId())
                .description(mealType.getDescription())
                .build();
    }

    private ParametricResponse buildParametricResponse(MealCategory mealCategory) {
        return ParametricResponse.builder()
                .id(mealCategory.getId())
                .description(mealCategory.getDescription())
                .build();
    }

    private ParametricResponse buildParametricResponse(PreparationTime preparationTime) {
        return ParametricResponse.builder()
                .id(preparationTime.getId())
                .description(preparationTime.getDescription())
                .build();
    }
}