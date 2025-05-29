package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.Ingredient;

import java.util.List;

public interface GetRecipesFromIngredientsRepository {
    String execute(List<Ingredient> ingredients);
}
