package com.cuoco.application.port.out;

public interface DeleteUserRecipeRepository {
    void execute(Long userId, Long recipeId);
}
