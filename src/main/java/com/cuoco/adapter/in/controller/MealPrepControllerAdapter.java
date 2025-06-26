package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.IngredientRequest;
import com.cuoco.adapter.in.controller.model.IngredientResponse;
import com.cuoco.adapter.in.controller.model.InstructionResponse;
import com.cuoco.adapter.in.controller.model.MealPrepFilterRequest;
import com.cuoco.adapter.in.controller.model.MealPrepRequest;
import com.cuoco.adapter.in.controller.model.MealPrepResponse;
import com.cuoco.adapter.in.controller.model.ParametricResponse;
import com.cuoco.adapter.in.controller.model.RecipeFilterRequest;
import com.cuoco.adapter.in.controller.model.UnitResponse;
import com.cuoco.application.port.in.GetMealPrepFromIngredientsCommand;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.CookLevel;
import com.cuoco.application.usecase.model.Diet;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.Instruction;
import com.cuoco.application.usecase.model.MealPrep;
import com.cuoco.application.usecase.model.MealPrepFilter;
import com.cuoco.application.usecase.model.MealType;
import com.cuoco.application.usecase.model.PreparationTime;
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
                .name(mealPrep.getName())
                .subtitle(mealPrep.getSubtitle())
                .recipes(mealPrep.getRecipes())
                .preparationTime(buildParametricResponse(mealPrep.getPreparationTime()))
                .cookLevel(buildParametricResponse(mealPrep.getCookLevel()))
                .diet(buildParametricResponse(mealPrep.getDiet()))
                .instructions(
                        mealPrep.getInstructions().stream().map(this::buildIntructionResponse).toList())
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

    private InstructionResponse buildIntructionResponse(Instruction instruction) {
        return InstructionResponse.builder()
                .title(instruction.getTitle())
                .time(instruction.getTime())
                .description(instruction.getDescription())
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
