package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.UserCalendar;

public interface CreateUserCalendarRepository {
    void execute(UserCalendar userCalendar);
}
