package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.User;

public interface SaveUserRecipeCommand {

    Boolean execute(SaveUserRecipeCommand.Command command);

    class Command {
        private User user;
        private Long recipeId;

        public Command(User user, Long recipeId) {
            this.user = user;
            this.recipeId = recipeId;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Long getRecipeId() {
            return recipeId;
        }

        public void setRecipeId(Long recipeId) {
            this.recipeId = recipeId;
        }
    }
}
