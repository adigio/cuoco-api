package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.SearchFilters;

import java.util.List;

public interface FindRecipesByFiltersRepository {
    List<Recipe> execute(SearchFilters filters);
}
