package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.Recipe;

import java.util.List;

public interface GetRandomRecipeCommand {
    List<Recipe> execute();
}