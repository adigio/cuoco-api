package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.Recipe;

public interface GetRecipeByIdQuery {
    Recipe execute(Long id, Integer servings);
}
