package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.Recipe;
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

        private Integer servings;
        private Integer preparationTimeId;
        private Integer cookLevelId;
        private Integer dietId;
        private List<Integer> typeIds;
        private List<Integer> allergiesIds;
        private List<Integer> dietaryNeedsIds;

        private Integer size;
        private List<Long> notInclude;
    }
}
