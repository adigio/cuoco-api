package com.cuoco.application.usecase;

import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.in.CreateOrUpdateUserRecipeCalendarCommand;
import com.cuoco.application.port.out.CreateUserCalendarRepository;
import com.cuoco.application.port.out.DeleteUserCalendarRepository;
import com.cuoco.application.port.out.GetMealTypeByIdRepository;
import com.cuoco.application.port.out.GetRecipeByIdRepository;
import com.cuoco.application.usecase.domainservice.CalendarDomainService;
import com.cuoco.application.usecase.domainservice.UserDomainService;
import com.cuoco.application.usecase.model.Calendar;
import com.cuoco.application.usecase.model.CalendarRecipe;
import com.cuoco.application.usecase.model.MealType;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserCalendar;
import com.cuoco.shared.model.ErrorDescription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateOrUpdateUserRecipeCalendarUseCase implements CreateOrUpdateUserRecipeCalendarCommand {

    private final UserDomainService userDomainService;
    private final CalendarDomainService calendarDomainService;
    private final CreateUserCalendarRepository createUserCalendarRepository;
    private final DeleteUserCalendarRepository deleteUserCalendarRepository;
    private final GetRecipeByIdRepository getRecipeByIdRepository;
    private final GetMealTypeByIdRepository getMealTypeByIdRepository;

    @Override
    public void execute(Command command) {
        User user = userDomainService.getCurrentUser();

        removeExistentCalendars(user);
        
        List<Calendar> requestedCalendars = command.getCalendars().stream()
                .peek(calendar -> calendar.setDate(toDateFormat(calendar.getDay().getId())))
                .filter(calendar -> !calendar.getRecipes().isEmpty())
                .toList();

        List<Calendar> calendarsToSave = requestedCalendars.stream()
                .map(this::buildCalendarWithValidatedRecipes)
                .toList();

        UserCalendar updated = UserCalendar.builder()
                .user(user)
                .calendars(calendarsToSave)
                .build();

        createUserCalendarRepository.execute(updated);
    }

    private void removeExistentCalendars(User user) {
        List<Calendar> currentCalendars = calendarDomainService.getUserCalendar(user);

        if (!currentCalendars.isEmpty()) {
            UserCalendar toDelete = UserCalendar.builder()
                    .user(user)
                    .calendars(currentCalendars)
                    .build();

            deleteUserCalendarRepository.execute(toDelete);
        }
    }

    private Calendar buildCalendarWithValidatedRecipes(Calendar calendarRequest) {
        List<CalendarRecipe> validatedRecipes = calendarRequest.getRecipes().stream()
                .map(this::buildRecipeCalendar)
                .toList();

        return Calendar.builder()
                .date(calendarRequest.getDate())
                .recipes(validatedRecipes)
                .build();
    }

    private CalendarRecipe buildRecipeCalendar(CalendarRecipe calendarRecipeRequest) {
        Recipe recipe = getRecipeByIdRepository.execute(calendarRecipeRequest.getRecipe().getId());
        MealType mealType = getMealTypeByIdRepository.execute(calendarRecipeRequest.getMealType().getId());

        return CalendarRecipe.builder()
                .recipe(recipe)
                .mealType(mealType)
                .build();
    }

    private LocalDate toDateFormat(int dayId) {
        if (dayId < 1 || dayId > 7) {
            throw new BadRequestException(ErrorDescription.INVALID_CALENDAR_DAY.getValue());
        }

        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeekToday = today.getDayOfWeek();
        int todayValue = dayOfWeekToday.getValue();

        int difference = dayId - todayValue;

        if (difference < 0) {
            difference += 7;
        }

        return today.plusDays(difference);
    }
}
