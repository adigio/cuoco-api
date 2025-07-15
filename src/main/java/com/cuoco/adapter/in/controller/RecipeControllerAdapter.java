package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.IngredientRequest;
import com.cuoco.adapter.in.controller.model.IngredientResponse;
import com.cuoco.adapter.in.controller.model.ParametricResponse;
import com.cuoco.adapter.in.controller.model.RecipeConfigurationRequest;
import com.cuoco.adapter.in.controller.model.RecipeFilterRequest;
import com.cuoco.adapter.in.controller.model.RecipeRequest;
import com.cuoco.adapter.in.controller.model.RecipeResponse;
import com.cuoco.adapter.in.controller.model.StepResponse;
import com.cuoco.adapter.in.utils.UtilsAdapter;
import com.cuoco.application.port.in.FindOrCreateRecipeCommand;
import com.cuoco.application.port.in.FindRecipesCommand;
import com.cuoco.application.port.in.GetRecipeByIdQuery;
import com.cuoco.application.port.in.GetRecipesFromIngredientsCommand;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.shared.GlobalExceptionHandler;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/recipes")
@Tag(name = "Recipes", description = "Obtains recipes with ingredients, filters and configuration")
public class RecipeControllerAdapter {

    private final GetRecipesFromIngredientsCommand getRecipesFromIngredientsCommand;
    private final GetRecipeByIdQuery getRecipeByIdQuery;
    private final FindOrCreateRecipeCommand findOrCreateRecipeCommand;
    private final FindRecipesCommand findRecipesCommand;

    public RecipeControllerAdapter(
            GetRecipesFromIngredientsCommand getRecipesFromIngredientsCommand,
            GetRecipeByIdQuery getRecipeByIdQuery,
            FindOrCreateRecipeCommand findOrCreateRecipeCommand,
            FindRecipesCommand findRecipesCommand
    ) {
        this.getRecipesFromIngredientsCommand = getRecipesFromIngredientsCommand;
        this.getRecipeByIdQuery = getRecipeByIdQuery;
        this.findOrCreateRecipeCommand = findOrCreateRecipeCommand;
        this.findRecipesCommand = findRecipesCommand;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get some specific recipe")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Return the specific recipe with the provided ID",
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
    public ResponseEntity<RecipeResponse> getRecipe(@PathVariable(name = "id") Long recipeId, @RequestParam(required = false) Integer servings) {
        log.info("Executing GET for find recipe with ID {}", recipeId);

        Recipe recipe = getRecipeByIdQuery.execute(recipeId, servings);

        RecipeResponse recipeResponse = buildResponse(recipe);

        log.info("Successfully obtained recipe with ID {}", recipeResponse.getId());
        return ResponseEntity.ok(recipeResponse);
    }

    @PostMapping
    @Operation(summary = "Find or create a recipe with the provided ingredients, filters and configuration")
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

    @GetMapping
    @Operation(summary = "Find or create a recipe from a specific provided name")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Return a recipe from the provided name",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecipeResponse.class)
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
    public ResponseEntity<RecipeResponse> quickRecipe(@RequestParam String name) {
        log.info("Executing find or generate recipe with name: {}", name);

        Recipe recipe = findOrCreateRecipeCommand.execute(buildQuickRecipeCommand(name));

        RecipeResponse response = buildResponse(recipe);

        log.info("Successfully found or generated recipe: {}", recipe.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/random")
    @Operation(summary = "Find random recipes with a provided size")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Return a recipe from the provided filter",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecipeResponse.class)
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
    public ResponseEntity<List<RecipeResponse>> getRecipes(@RequestParam Integer size) {
        log.info("Executing find recipes with size: {}", size);

        List<Recipe> recipes = findRecipesCommand.execute(buildFindRecipesCommand(size));

        List<RecipeResponse> recipesResponse = recipes.stream().map(this::buildResponse).toList();

        log.info("Successfully found recipes");
        return ResponseEntity.ok(recipesResponse);
    }

    private GetRecipesFromIngredientsCommand.Command buildGenerateRecipeCommand(RecipeRequest recipeRequest) {

        boolean filtersEnabled = true;

        if(recipeRequest.getFilters() == null) {
            filtersEnabled = false;
            recipeRequest.setFilters(new RecipeFilterRequest());
        }

        if(recipeRequest.getConfiguration() == null) recipeRequest.setConfiguration(new RecipeConfigurationRequest());

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

    private FindOrCreateRecipeCommand.Command buildQuickRecipeCommand(String name) {
        return FindOrCreateRecipeCommand.Command.builder()
                .recipeName(name)
                .build();
    }

    private FindRecipesCommand.Command buildFindRecipesCommand(Integer size) {
        return FindRecipesCommand.Command.builder()
                .size(size)
                .random(true)
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
                .favorite(recipe.getFavorite())
                .steps(recipe.getSteps().stream().map(StepResponse::fromDomain).toList())
                .image(recipe.getImage())
                .preparationTime(ParametricResponse.fromDomain(recipe.getPreparationTime()))
                .cookLevel(ParametricResponse.fromDomain(recipe.getCookLevel()))
                .diet(UtilsAdapter.mapNull(recipe.getDiet()))
                .mealTypes(recipe.getMealTypes().stream().map(ParametricResponse::fromDomain).toList())
                .allergies(UtilsAdapter.mapNullOrEmpty(recipe.getAllergies()))
                .dietaryNeeds(UtilsAdapter.mapNullOrEmpty(recipe.getDietaryNeeds()))
                .ingredients(recipe.getIngredients().stream().map(IngredientResponse::fromDomain).toList())
                .build();
    }
}