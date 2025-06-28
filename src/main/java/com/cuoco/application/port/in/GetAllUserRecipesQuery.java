package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.Recipe;

import java.util.List;

public interface GetAllUserRecipesQuery {
    List<Recipe> execute();
}
