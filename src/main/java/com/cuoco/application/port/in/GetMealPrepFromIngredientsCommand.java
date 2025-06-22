package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.MealPrep;
import com.cuoco.application.usecase.model.MealPrepFilter;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

public interface GetMealPrepFromIngredientsCommand {

    List<MealPrep> execute(Command command);

    @Data
    @Builder
    @ToString
    class Command {
        private MealPrepFilter filters;
        private List<Ingredient> ingredients;
    }
}

