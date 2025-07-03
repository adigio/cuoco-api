package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.Calendar;

import java.util.List;

public interface GetUserCalendarByUserIdRepository {
    List<Calendar> execute(Long id);

}
