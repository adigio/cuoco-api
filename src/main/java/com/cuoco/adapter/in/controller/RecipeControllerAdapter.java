package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.out.rest.model.gemini.GeminiResponseMapper;
import com.cuoco.adapter.in.controller.model.RecipeRequest;
import com.cuoco.application.port.in.GetRecipesFromIngredientsCommand;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.RecipeFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/generate-recipes")
public class RecipeControllerAdapter {

    static final Logger log = LoggerFactory.getLogger(RecipeControllerAdapter.class);

    private final GetRecipesFromIngredientsCommand getRecipesFromIngredientsCommand;
    private final GeminiResponseMapper geminiResponseMapper;

    public RecipeControllerAdapter(GetRecipesFromIngredientsCommand getRecipesFromIngredientsCommand,
                                                        GeminiResponseMapper geminiResponseMapper) {
        this.getRecipesFromIngredientsCommand = getRecipesFromIngredientsCommand;
        this.geminiResponseMapper = geminiResponseMapper;
    }

    @PostMapping()
    public ResponseEntity<?> generate(@RequestBody RecipeRequest recipeRequest) {
        try {
            log.info("Executing GET recipes from ingredients with body {}", recipeRequest);

            String recipes = getRecipesFromIngredientsCommand.execute(buildGenerateRecipeCommand(recipeRequest));

            log.info("Successfully generated recipes");

            // Use dedicated mapper to parse Gemini response
            Object jsonResponse = geminiResponseMapper.parseToJson(recipes);
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            log.error("Error generating recipes: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error al generar la receta: " + e.getMessage());
        }
    }

    private GetRecipesFromIngredientsCommand.Command buildGenerateRecipeCommand(RecipeRequest recipeRequest) {
        return new GetRecipesFromIngredientsCommand.Command(
                recipeRequest.getFilters() != null ?
                        new RecipeFilter(
                                recipeRequest.getFilters().getTime(),
                                recipeRequest.getFilters().getDifficulty(),
                                recipeRequest.getFilters().getTypes(),
                                recipeRequest.getFilters().getDiet(),
                                recipeRequest.getFilters().getQuantity()
                        ) : null,
                recipeRequest.getIngredients().stream().map(ingredient ->
                        new Ingredient(
                                ingredient.getName(),
                                ingredient.getSource(),
                                ingredient.isConfirmed()
                        )
                ).toList()
        );
    }
}