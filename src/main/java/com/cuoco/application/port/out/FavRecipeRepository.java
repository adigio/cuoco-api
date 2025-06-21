package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.UserRecipe;

public interface FavRecipeRepository {

    Boolean execute(UserRecipe userRecipe);

    class Command {
        private String user;

        private String title;

        private String ingredient;

        public Command(String user, String title, String ingredient) {
            this.user = user;
            this.title = title;
            this.ingredient = ingredient;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIngredient() {
            return ingredient;
        }

        public void setIngredient(String ingredient) {
            this.ingredient = ingredient;
        }

        @Override
        public String toString() {
            return "Command{" +
                    "user='" + user + '\'' +
                    ", title='" + title + '\'' +
                    ", ingredient='" + ingredient + '\'' +
                    '}';
        }
    }
}
