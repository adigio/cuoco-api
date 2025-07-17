package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetUserCalendarQuery;
import com.cuoco.application.usecase.domainservice.CalendarDomainService;
import com.cuoco.application.usecase.domainservice.UserDomainService;
import com.cuoco.application.usecase.model.Calendar;
import com.cuoco.application.usecase.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetUserCalendarUseCase implements GetUserCalendarQuery {

    private final UserDomainService userDomainService;
    private final CalendarDomainService calendarDomainService;

    @Override
    public List<Calendar> execute() {
        log.info("Executing get user calendar use case");

        User user = userDomainService.getCurrentUser();

        List<Calendar> calendarRecipes = calendarDomainService.getUserCalendar(user);

        log.info("Successfully retrieved user calendar with {} dates", calendarRecipes.size());
        return calendarRecipes;
    }

}
