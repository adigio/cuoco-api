package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetUserRecipeCalendarCommand;
import com.cuoco.application.port.out.GetUserRecipeCalendarRepository;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserRecipeCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetUserRecipeCalendarUseCase implements GetUserRecipeCalendarCommand {

    static final Logger log = LoggerFactory.getLogger(GetUserRecipeCalendarUseCase.class);

    private GetUserRecipeCalendarRepository getUserRecipeCalendarRepository;

    public GetUserRecipeCalendarUseCase(GetUserRecipeCalendarRepository getUserRecipeCalendarRepository) {
        this.getUserRecipeCalendarRepository = getUserRecipeCalendarRepository;
    }


    @Override
    public List<UserRecipeCalendar> execute() {
        User user = null;
        try {
            user = validateUser();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<UserRecipeCalendar> calendarRecipes = getUserRecipeCalendarRepository.execute(user.getId());

        calendarRecipes = limitArrayToThisWeekOnly(calendarRecipes);

        log.info("User recipes: {}", calendarRecipes.size() );

        return calendarRecipes;
    }

    private List<UserRecipeCalendar> limitArrayToThisWeekOnly(List<UserRecipeCalendar> calendarRecipes) {
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysLater = today.plusDays(7);


        return calendarRecipes.stream()
                .filter(urc -> {
                    LocalDate Date = urc.getDate();
                    return (Date != null && !Date.isBefore(today) && !Date.isAfter(sevenDaysLater));
                })
                .collect(Collectors.toList());



    }


    private User validateUser() throws Exception {
        User user=null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof User){
            user = (User) principal;
            log.info("User: {}", user.getName());
        }

        if(user==null) {
            throw new Exception("User not found. Please log in to save a recipe.");
        }
        return user;
    }
}
