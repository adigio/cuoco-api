package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.Step;

import java.util.List;

public interface GetRecipeStepsImagesRepository {
    List<Step> execute(Recipe recipe);
} 