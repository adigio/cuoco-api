package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.Calendar;

import java.util.List;

public interface GetUserCalendarQuery {
    List<Calendar> execute();
}
