package com.cuoco.adapter.in.controller.model;

import com.cuoco.adapter.in.controller.model.IngredientsResponse;
import com.cuoco.application.usecase.model.Ingredient;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class IngredientsResponseMapper {

    public List<String> toSimpleStringArray(IngredientsResponse ingredientsResponse) {
        if (ingredientsResponse == null || ingredientsResponse.getIngredients() == null) {
            return List.of();
        }

        return ingredientsResponse.getIngredients()
                .stream()
                .map(Ingredient::getName)
                .collect(Collectors.toList());
    }

    public IngredientsResponse toImageSeparateResponse(Map<String, List<Ingredient>> ingredientsByImage) {
        if (ingredientsByImage == null || ingredientsByImage.isEmpty()) {
            return new IngredientsResponse(Map.of());
        }

        Map<String, List<String>> mappedIngredients = ingredientsByImage.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(Ingredient::getName)
                                .collect(Collectors.toList())
                ));

        return new IngredientsResponse(mappedIngredients);
    }


    public IngredientsResponse toResponse(List<Ingredient> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            return new IngredientsResponse(Collections.emptyList());
        }
        return new IngredientsResponse(ingredients);
    }

}