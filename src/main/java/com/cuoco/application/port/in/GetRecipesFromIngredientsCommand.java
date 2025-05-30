package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.RecipeFilter;

import java.util.List;

public interface GetRecipesFromIngredientsCommand {

    String execute(Command command);

    class Command {

        private final RecipeFilter filters;
        private final List<Ingredient> ingredients;

        public Command(RecipeFilter filters, List<Ingredient> ingredients) {
            this.filters = filters;
            this.ingredients = ingredients;
        }

        public RecipeFilter getFilters() {
            return filters;
        }

        public List<Ingredient> getIngredients() {
            return ingredients;
        }

        @Override
        public String toString() {
            return "Command{" +
                    "filters=" + filters +
                    ", ingredients=" + ingredients +
                    '}';
        }
    }

}
