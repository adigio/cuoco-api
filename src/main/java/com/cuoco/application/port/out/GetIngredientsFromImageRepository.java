package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.File;
import com.cuoco.application.usecase.model.Ingredient;

import java.util.List;

public interface GetIngredientsFromImageRepository {
    List<Ingredient> execute(List<File> files);
}