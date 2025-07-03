package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.IngredientRequest;
import com.cuoco.adapter.in.controller.model.IngredientResponse;
import com.cuoco.adapter.in.controller.model.MealPrepRequest;
import com.cuoco.adapter.in.controller.model.MealPrepResponse;
import com.cuoco.adapter.in.controller.model.ParametricResponse;
import com.cuoco.adapter.in.controller.model.RecipeResponse;
import com.cuoco.adapter.in.controller.model.StepResponse;
import com.cuoco.adapter.in.controller.model.UnitResponse;
import com.cuoco.application.port.in.GetMealPrepFromIngredientsCommand;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.MealPrep;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.Step;
import com.cuoco.shared.GlobalExceptionHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Meal Prep", description = "Obtains recipes for MealPrep from ingredients")
public class MealPrepControllerAdapter {

    private final GetMealPrepFromIngredientsCommand getMealPrepFromIngredientsCommand;

    public MealPrepControllerAdapter(GetMealPrepFromIngredientsCommand getMealPrepFromIngredientsCommand) {
        this.getMealPrepFromIngredientsCommand = getMealPrepFromIngredientsCommand;
    }

    @PostMapping
    @Operation(summary = "Create meal preps")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Return all the created meal types",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MealPrepResponse.class))
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
    public ResponseEntity<List<MealPrepResponse>> generate(@RequestBody MealPrepRequest mealPrepRequest){

        log.info("Executing GET mealPrep from ingredients with body {}", mealPrepRequest);

        List<MealPrep> mealPreps = getMealPrepFromIngredientsCommand.execute(buildGenerateMealPrepCommand(mealPrepRequest));

        List<MealPrepResponse> mealPrepsResponse = mealPreps.stream().map(this::buildResponse).toList();

        log.info("Successfully generated recipes");
        return ResponseEntity.ok(mealPrepsResponse);
    }

    private GetMealPrepFromIngredientsCommand.Command buildGenerateMealPrepCommand(MealPrepRequest mealPrepRequest) {
        return GetMealPrepFromIngredientsCommand.Command.builder()
                .ingredients(mealPrepRequest.getIngredients().stream().map(this::buildIngredient).toList())
                .freeze(mealPrepRequest.getFilters().getFreeze())
                .preparationTimeId(mealPrepRequest.getFilters().getPreparationTimeId())
                .servings(mealPrepRequest.getFilters().getServings())
                .cookLevelId(mealPrepRequest.getFilters().getCookLevelId())
                .dietId(mealPrepRequest.getFilters().getDietId())
                .typeIds(mealPrepRequest.getFilters().getTypeIds())
                .allergiesIds(mealPrepRequest.getFilters().getAllergiesIds())
                .dietaryNeedsIds(mealPrepRequest.getFilters().getDietaryNeedsIds())
                .build();
    }

    private Ingredient buildIngredient(IngredientRequest ingredientRequest) {
        return Ingredient.builder()
                .name(ingredientRequest.getName())
                .build();
    }

    private MealPrepResponse buildResponse(MealPrep mealPrep) {
        return MealPrepResponse.builder()
                .id(mealPrep.getId())
                .title(mealPrep.getTitle())
                .estimatedCookingTime(mealPrep.getEstimatedCookingTime())
                .servings(mealPrep.getServings())
                .freeze(mealPrep.getFreeze())
                .steps(mealPrep.getSteps().stream().map(StepResponse::fromDomain).toList())
                .recipes(mealPrep.getRecipes().stream().map(this::buildRecipeResponse).toList())
                .ingredients(mealPrep.getIngredients().stream().map(IngredientResponse::fromDomain).toList())
                .build();
    }

    private RecipeResponse buildRecipeResponse(Recipe recipe) {
        return RecipeResponse.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .subtitle(recipe.getSubtitle())
                .description(recipe.getDescription())
                .image(recipe.getImage())
                .build();
    }
}
