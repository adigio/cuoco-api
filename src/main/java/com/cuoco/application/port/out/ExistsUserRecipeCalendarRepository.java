package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.Calendar;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.User;

public interface ExistsUserRecipeCalendarRepository {
    Boolean execute(User user, Calendar calendar, Recipe recipe);
}
