package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.Recipe;

public interface FindRecipeByNameRepository {
    Recipe execute(String recipeName);
} 