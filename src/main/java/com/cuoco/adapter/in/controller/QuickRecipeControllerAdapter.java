package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.IngredientResponse;
import com.cuoco.adapter.in.controller.model.ParametricResponse;
import com.cuoco.adapter.in.controller.model.QuickRecipeRequest;
import com.cuoco.adapter.in.controller.model.RecipeResponse;
import com.cuoco.adapter.in.controller.model.UnitResponse;
import com.cuoco.application.port.in.FindOrGenerateRecipeCommand;
import com.cuoco.application.usecase.model.Ingredient;
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