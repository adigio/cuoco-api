package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.IngredientRequest;
import com.cuoco.adapter.in.controller.model.IngredientResponse;
import com.cuoco.adapter.in.controller.model.ParametricResponse;
import com.cuoco.adapter.in.controller.model.RecipeConfiguration;
import com.cuoco.adapter.in.controller.model.RecipeFilterRequest;
import com.cuoco.adapter.in.controller.model.RecipeRequest;
import com.cuoco.adapter.in.controller.model.RecipeResponse;
import com.cuoco.adapter.in.controller.model.StepResponse;
import com.cuoco.adapter.in.controller.model.UnitResponse;
import com.cuoco.adapter.in.utils.Utils;
import com.cuoco.application.port.in.GetRandomRecipeCommand;
import com.cuoco.application.port.in.GetRecipeByIdQuery;
import com.cuoco.application.port.in.GetRecipesFromIngredientsCommand;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.CookLevel;
import com.cuoco.application.usecase.model.Diet;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.MealType;
import com.cuoco.application.usecase.model.PreparationTime;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.Step;
import com.cuoco.shared.GlobalExceptionHandler;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/recipes")
@Tag(name = "Recipes", description = "Obtains recipes from ingredients, filters and configuration")
public class RecipeControllerAdapter {

    private final GetRecipesFromIngredientsCommand getRecipesFromIngredientsCommand;
    private final GetRecipeByIdQuery getRecipeByIdQuery;
    private final GetRandomRecipeCommand getRandomRecipeCommand;

    public RecipeControllerAdapter(
            GetRecipesFromIngredientsCommand getRecipesFromIngredientsCommand,
            GetRecipeByIdQuery getRecipeByIdQuery,
            GetRandomRecipeCommand getRandomRecipeCommand
    ) {
        this.getRecipesFromIngredientsCommand = getRecipesFromIngredientsCommand;
        this.getRecipeByIdQuery = getRecipeByIdQuery;
        this.getRandomRecipeCommand = getRandomRecipeCommand;
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Return the specific recipe with the provided ID",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RecipeResponse.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recipe not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Service unavailable",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ApiErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<RecipeResponse> getRecipe(@PathVariable(name = "id") Long recipeId) {
        log.info("Executing GET for find recipe with ID {}", recipeId);

        Recipe recipe = getRecipeByIdQuery.execute(recipeId);

        RecipeResponse recipeResponse = buildResponse(recipe);

        log.info("Successfully obtained recipe with ID {}", recipeResponse.getId());
        return ResponseEntity.ok(recipeResponse);
    }

    @PostMapping()
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Return a list of recipes with the provided ingredients and filters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecipeResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recipe not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Service unavailable",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ApiErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<List<RecipeResponse>> generate(@RequestBody @Valid RecipeRequest recipeRequest) {

        log.info("Executing POST for get or creation of recipes by ingredients with body {}", recipeRequest);

        List<Recipe> recipes = getRecipesFromIngredientsCommand.execute(buildGenerateRecipeCommand(recipeRequest));

        List<RecipeResponse> recipesResponse = recipes.stream().map(this::buildResponse).toList();

        log.info("Successfully generated recipes");
        return ResponseEntity.ok(recipesResponse);
    }

    @GetMapping("/random")
    public ResponseEntity<List<RecipeResponse>> generateRandomRecipe() {
        log.info("Executing GET for get random recipe");
        List<Recipe> recipes = getRandomRecipeCommand.execute();
        return buildRandomResponse(recipes);
    }

    private ResponseEntity<List<RecipeResponse>> buildRandomResponse(List<Recipe> recipes) {
        List<RecipeResponse> responses = recipes.stream()
                .map(recipe -> RecipeResponse.builder()
                        .id(recipe.getId())
                        .name(recipe.getName())
                        .subtitle(recipe.getSubtitle())
                        .description(recipe.getDescription())
                        .steps(recipe.getSteps().stream().map(StepResponse::fromDomain).toList())
                        .image(recipe.getImage())
                        .preparationTime(ParametricResponse.fromDomain(recipe.getPreparationTime()))
                        .cookLevel(ParametricResponse.fromDomain(recipe.getCookLevel()))
                        .diet(Utils.mapNull(recipe.getDiet()))
                        .mealTypes(recipe.getMealTypes().stream().map(ParametricResponse::fromDomain).toList())
                        .allergies(Utils.mapNullOrEmpty(recipe.getAllergies()))
                        .dietaryNeeds(Utils.mapNullOrEmpty(recipe.getDietaryNeeds()))
                        .ingredients(recipe.getIngredients().stream().map(IngredientResponse::fromDomain).toList())
                        .build()
                ).collect(Collectors.toList());
        return ResponseEntity.ok(responses);
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
        return RecipeResponse.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .subtitle(recipe.getSubtitle())
                .description(recipe.getDescription())
                .steps(recipe.getSteps().stream().map(StepResponse::fromDomain).toList())
                .image(recipe.getImage())
                .preparationTime(ParametricResponse.fromDomain(recipe.getPreparationTime()))
                .cookLevel(ParametricResponse.fromDomain(recipe.getCookLevel()))
                .diet(Utils.mapNull(recipe.getDiet()))
                .mealTypes(recipe.getMealTypes().stream().map(ParametricResponse::fromDomain).toList())
                .allergies(Utils.mapNullOrEmpty(recipe.getAllergies()))
                .dietaryNeeds(Utils.mapNullOrEmpty(recipe.getDietaryNeeds()))
                .ingredients(recipe.getIngredients().stream().map(IngredientResponse::fromDomain).toList())
                .build();
    }
}