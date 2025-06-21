package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.UserRecipe;

public interface SavedRecipeExistByUsernameRepository {
    //todo poner interior de execute
    boolean execute(UserRecipe userRecipe);


}
