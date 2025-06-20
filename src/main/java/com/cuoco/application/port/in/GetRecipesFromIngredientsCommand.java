package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.RecipeFilter;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

public interface GetRecipesFromIngredientsCommand {

    List<Recipe> execute(Command command);

    @Data
    @Builder
    @ToString
    class Command {
        private List<Ingredient> ingredients;

        private Boolean filtersEnabled;
        private Integer preparationTimeId;
        private Integer servings;
        private Integer cookLevelId;
        private List<Integer> typeIds;
        private List<Integer> categoryIds;
        private Boolean useProfilePreferences;

        private Integer recipesSize;
        private List<Long> notInclude;
    }
}
