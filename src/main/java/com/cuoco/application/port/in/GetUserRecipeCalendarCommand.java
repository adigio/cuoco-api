package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.UserRecipe;
import com.cuoco.application.usecase.model.UserRecipeCalendar;

import java.util.List;

public interface GetUserRecipeCalendarCommand {
    List<UserRecipeCalendar> execute();
}
