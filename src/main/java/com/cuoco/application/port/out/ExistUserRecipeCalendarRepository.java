package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.UserRecipeCalendar;

public interface ExistUserRecipeCalendarRepository {
    Boolean execute(UserRecipeCalendar userRecipeCalendar);
}
