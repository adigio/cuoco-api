package com.cuoco.application.usecase.model;

public class UserRecipe {

    private User user;
    private Recipe recipe;
    private boolean favorite;

    public UserRecipe(User user, Recipe recipe, boolean favorite) {
        this.user = user;
        this.recipe = recipe;
        this.favorite = favorite;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
