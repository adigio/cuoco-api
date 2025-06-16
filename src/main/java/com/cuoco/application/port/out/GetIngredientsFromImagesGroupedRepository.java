package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.File;
import com.cuoco.application.usecase.model.Ingredient;

import java.util.List;
import java.util.Map;

public interface GetIngredientsFromImagesGroupedRepository {
    Map<String, List<Ingredient>> execute(List<File> files);
}
