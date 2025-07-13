package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.UserCalendar;

public interface DeleteUserCalendarRepository {
    void execute(UserCalendar userCalendar);
}
