package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.UserCalendar;

import java.time.LocalDate;

public interface FindUserCalendarByUserIdAndDateRepository {
    UserCalendar execute(Long userId, LocalDate plannedDate);
}
