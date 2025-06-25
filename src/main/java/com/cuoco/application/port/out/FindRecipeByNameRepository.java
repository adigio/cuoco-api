package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.Recipe;

import java.util.Optional;

public interface FindRecipeByNameRepository {
    Optional<Recipe> execute(String recipeName);
} 