package com.cuoco.application.usecase.domainservice;

import com.cuoco.application.port.out.GetUserCalendarByUserIdRepository;
import com.cuoco.application.usecase.model.Calendar;
import com.cuoco.application.usecase.model.Day;
import com.cuoco.application.usecase.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class CalendarDomainService {

    private final GetUserCalendarByUserIdRepository getUserCalendarByUserIdRepository;

    public List<Calendar> getUserCalendar(User user) {
        log.info("Getting calendars for user with id {} in calendar domain service", user.getId());

        List<Calendar> calendarRecipes = getUserCalendarByUserIdRepository.execute(user.getId());

        calendarRecipes = dropPastDates(calendarRecipes);

        return calendarRecipes;
    }

    private List<Calendar> dropPastDates(List<Calendar> calendar) {
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysLater = today.plusDays(7);

        return calendar.stream()
                .filter(urc -> dropPastDates(urc, today, sevenDaysLater))
                .map(this::normalizeDate)
                .collect(Collectors.toList());
    }

    private Calendar normalizeDate(Calendar calendar) {
        DayOfWeek dayOfWeek = calendar.getDate().getDayOfWeek();
        String description = capitalize(dayOfWeek.getDisplayName(TextStyle.FULL, new Locale("es")));

        calendar.setDay(Day.builder().id(dayOfWeek.getValue()).description(description).build());

        return calendar;
    }

    private static boolean dropPastDates(Calendar urc, LocalDate today, LocalDate sevenDaysLater) {
        LocalDate Date = urc.getDate();
        return (Date != null && !Date.isBefore(today) && !Date.isAfter(sevenDaysLater));
    }

    private String capitalize(String word) {
        if (word == null || word.isEmpty()) return word;
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

}
