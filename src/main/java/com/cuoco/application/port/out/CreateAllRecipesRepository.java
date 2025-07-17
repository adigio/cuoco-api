package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.Recipe;

import java.util.List;

public interface CreateAllRecipesRepository {
    List<Recipe> execute(List<Recipe> recipes);
}
