package com.cuoco.application.usecase;

import com.cuoco.application.port.in.CreateUserRecipeCalendarCommand;
import com.cuoco.application.port.out.ExistsUserRecipeCalendarRepository;
import com.cuoco.application.port.out.GetMealTypeByIdRepository;
import com.cuoco.application.port.out.GetRecipeByIdRepository;
import com.cuoco.application.port.out.CreateUserCalendarRepository;
import com.cuoco.application.usecase.domainservice.UserDomainService;
import com.cuoco.application.usecase.model.Calendar;
import com.cuoco.application.usecase.model.MealType;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.CalendarRecipe;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserCalendar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Slf4j
@Component
public class CreateUserRecipeCalendarUseCase implements CreateUserRecipeCalendarCommand {

    private final UserDomainService userDomainService;
    private final CreateUserCalendarRepository createUserCalendarRepository;
    private final ExistsUserRecipeCalendarRepository existsUserRecipeCalendarRepository;
    private final GetRecipeByIdRepository getRecipeByIdRepository;
    private final GetMealTypeByIdRepository getMealTypeByIdRepository;

    public CreateUserRecipeCalendarUseCase(
            UserDomainService userDomainService,
            CreateUserCalendarRepository createUserCalendarRepository,
            ExistsUserRecipeCalendarRepository existsUserRecipeCalendarRepository,
            GetRecipeByIdRepository getRecipeByIdRepository,
            GetMealTypeByIdRepository getMealTypeByIdRepository
    ) {
        this.userDomainService = userDomainService;
        this.createUserCalendarRepository = createUserCalendarRepository;
        this.existsUserRecipeCalendarRepository = existsUserRecipeCalendarRepository;
        this.getRecipeByIdRepository = getRecipeByIdRepository;
        this.getMealTypeByIdRepository = getMealTypeByIdRepository;
    }

    @Override
    public void execute(Command command) {
        log.info("Executing user recipe calendar creation use case");

        User user = userDomainService.getCurrentUser();

        UserCalendar userCalendar = buildUserRecipeCalendar(command, user);

        createUserCalendarRepository.execute(userCalendar);
    }

    private UserCalendar buildUserRecipeCalendar(Command command, User user) {
        return UserCalendar.builder()
                .user(user)
                .calendars(command.getCalendarCommands().stream().map(this::buildCalendar).toList())
                .build();
    }

    private Calendar buildCalendar(Command.CalendarCommand calendarCommand) {
        LocalDate date = toDateFormat(calendarCommand.getDayId());

        return Calendar.builder()
                .date(date)
                .recipes(calendarCommand.getCalendarRecipeCommands().stream().map(this::buildRecipeCalendar).toList())
                .build();
    }

    private CalendarRecipe buildRecipeCalendar(Command.CalendarRecipeCommand calendarRecipeCommand) {
        Recipe recipe = getRecipeByIdRepository.execute(calendarRecipeCommand.getRecipeId());
        MealType mealType = getMealTypeByIdRepository.execute(calendarRecipeCommand.getMealtypeId());

        return CalendarRecipe.builder()
                .recipe(recipe)
                .mealType(mealType)
                .build();
    }

    private LocalDate toDateFormat(int dayId) {
        if (dayId < 1 || dayId > 7) {
            throw new IllegalArgumentException("Not between the seven days of the week");
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
