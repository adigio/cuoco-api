package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.UserRecipe;

import java.util.List;

public interface GetUserRecipeCommand {
    List<UserRecipe> execute();

}
