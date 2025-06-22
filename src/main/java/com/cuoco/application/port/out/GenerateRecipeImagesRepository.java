package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.RecipeImage;

import java.util.List;

public interface GenerateRecipeImagesRepository {
    List<RecipeImage> execute(Recipe recipe);
} 