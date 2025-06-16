package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.File;
import com.cuoco.application.usecase.model.Ingredient;

import java.util.List;
import java.util.Map;

public interface GetIngredientsGroupedFromImagesRepository {
    Map<String, List<Ingredient>> execute(List<File> files);
}
