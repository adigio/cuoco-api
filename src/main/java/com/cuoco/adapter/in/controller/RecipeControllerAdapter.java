package com.cuoco.adapter.in.controller;

import com.cuoco.application.port.in.GetRecipesFromIngredientsCommand;
import com.cuoco.adapter.in.controller.model.RecipeRequest;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.RecipeFilter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recipes")
public class RecipeControllerAdapter {

    private final GetRecipesFromIngredientsCommand getRecipesFromIngredientsCommand;

    public RecipeControllerAdapter(GetRecipesFromIngredientsCommand getRecipesFromIngredientsCommand) {
        this.getRecipesFromIngredientsCommand = getRecipesFromIngredientsCommand;
    }

    @PostMapping()
    public ResponseEntity<?> generate(@RequestBody RecipeRequest recipeRequest) {
        try {
            String receta = getRecipesFromIngredientsCommand.execute(buildGenerateRecipeCommand(recipeRequest));

            return ResponseEntity.ok(receta);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al generar la receta: " + e.getMessage());
        }
    }

    private GetRecipesFromIngredientsCommand.Command buildGenerateRecipeCommand(RecipeRequest recipeRequest) {
        return new GetRecipesFromIngredientsCommand.Command(
                new RecipeFilter(
                        recipeRequest.getFilters().getTime(),
                        recipeRequest.getFilters().getDifficulty(),
                        recipeRequest.getFilters().getTypes(),
                        recipeRequest.getFilters().getDiet(),
                        recipeRequest.getFilters().getQuantity()
                ),
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
