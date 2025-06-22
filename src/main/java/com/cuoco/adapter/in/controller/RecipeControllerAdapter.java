package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.IngredientRequest;
import com.cuoco.adapter.in.controller.model.IngredientResponse;
import com.cuoco.adapter.in.controller.model.ParametricResponse;
import com.cuoco.adapter.in.controller.model.RecipeFilterRequest;
import com.cuoco.adapter.in.controller.model.RecipeImageResponse;
import com.cuoco.adapter.in.controller.model.RecipeRequest;
import com.cuoco.adapter.in.controller.model.RecipeResponse;
import com.cuoco.adapter.in.controller.model.UnitResponse;
import com.cuoco.application.port.in.GenerateRecipeImagesCommand;
import com.cuoco.application.port.in.GetRecipesFromIngredientsCommand;
import com.cuoco.application.usecase.model.CookLevel;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.RecipeFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    @Autowired(required = false)
    private GenerateRecipeImagesCommand generateRecipeImagesCommand;

    public RecipeControllerAdapter(GetRecipesFromIngredientsCommand getRecipesFromIngredientsCommand) {
        this.getRecipesFromIngredientsCommand = getRecipesFromIngredientsCommand;
    }

    @PostMapping()
    public ResponseEntity<List<RecipeResponse>> generate(@RequestBody RecipeRequest recipeRequest) {

        log.info("Executing GET recipes from ingredients with body {}", recipeRequest);

        List<Recipe> recipes = getRecipesFromIngredientsCommand.execute(buildGenerateRecipeCommand(recipeRequest));

        List<RecipeResponse> recipesResponse = recipes.stream().map(this::buildResponse).toList();

        log.info("Successfully generated recipes");
        return ResponseEntity.ok(recipesResponse);
    }

    private GetRecipesFromIngredientsCommand.Command buildGenerateRecipeCommand(RecipeRequest recipeRequest) {
        return GetRecipesFromIngredientsCommand.Command.builder()
                .filters(recipeRequest.getFilters() != null ? buildFilter(recipeRequest.getFilters()) : null)
                .ingredients(recipeRequest.getIngredients().stream().map(this::buildIngredient).toList())
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

    private RecipeResponse buildResponse(Recipe recipe) {
        return RecipeResponse.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .image(recipe.getImage())
                .subtitle(recipe.getSubtitle())
                .description(recipe.getDescription())
                .preparationTime(recipe.getPreparationTime())
                .instructions(recipe.getInstructions())
                .ingredients(
                        recipe.getIngredients().stream().map(this::buildIngredientResponse).toList()
                )
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