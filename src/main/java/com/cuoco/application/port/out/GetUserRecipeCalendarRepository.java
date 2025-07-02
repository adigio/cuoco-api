package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.UserRecipeCalendar;

import java.util.List;

public interface GetUserRecipeCalendarRepository {
    List<UserRecipeCalendar> execute(Long id);

}
