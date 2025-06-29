package com.cuoco.application.usecase;

import com.cuoco.application.port.in.SaveUserRecipeCalendarCommand;
import com.cuoco.application.port.out.ExistUserRecipeCalendarRepository;
import com.cuoco.application.port.out.GetMealTypeByIdRepository;
import com.cuoco.application.port.out.GetRecipeByIdRepository;
import com.cuoco.application.port.out.SaveUserRecipeCalendarRepository;
import com.cuoco.application.usecase.model.MealType;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserRecipeCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class SaveUserRecipeCalendarUseCase implements SaveUserRecipeCalendarCommand {

    static final Logger log = LoggerFactory.getLogger(SaveUserRecipeCalendarUseCase.class);

    private SaveUserRecipeCalendarRepository saveUserRecipeCalendarRepository;

    private ExistUserRecipeCalendarRepository existUserRecipeCalendarRepository;

    private GetRecipeByIdRepository getRecipeByIdRepository;

    private GetMealTypeByIdRepository getMealTypeByIdRepository;

    public SaveUserRecipeCalendarUseCase(SaveUserRecipeCalendarRepository saveUserRecipeCalendarRepository,
                                         ExistUserRecipeCalendarRepository existUserRecipeCalendarRepository,
                                         GetRecipeByIdRepository getRecipeByIdRepository,
                                         GetMealTypeByIdRepository getMealTypeByIdRepository) {
        this.saveUserRecipeCalendarRepository = saveUserRecipeCalendarRepository;
        this.existUserRecipeCalendarRepository = existUserRecipeCalendarRepository;
        this.getRecipeByIdRepository = getRecipeByIdRepository;
        this.getMealTypeByIdRepository = getMealTypeByIdRepository;
    }

    @Override
    public Boolean execute(Command command) {
        List<UserRecipeCalendar> userRecipeCalendars = new ArrayList<>();

        for(Object ob: command.getCalendarCommands()){
            if(!(ob instanceof SaveUserRecipeCalendarCommand.Command.CalendarCommand)){
                throw new RuntimeException("Invalid calendar command");
            }
            SaveUserRecipeCalendarCommand.Command.CalendarCommand calendarCommand = (SaveUserRecipeCalendarCommand.Command.CalendarCommand) ob;

            UserRecipeCalendar userRecipeCalendar = null;
            try {
                userRecipeCalendar = validateUserRecipeCalendar(calendarCommand);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if(userRecipeCalendar!=null && !existUserRecipeCalendarRepository.execute(userRecipeCalendar)){
                userRecipeCalendars.add(userRecipeCalendar);

            }
        }

        if(!userRecipeCalendars.isEmpty()){
            saveUserRecipeCalendarRepository.execute(userRecipeCalendars);
            return true;
        }
    return false;
    }

    private UserRecipeCalendar validateUserRecipeCalendar(Command.CalendarCommand calendarCommand) throws Exception {
        Recipe recipe = validateToRecipe(calendarCommand.getRecipeId());
        MealType mealType = validateMealtype(calendarCommand.getMealtypeId());
        LocalDate date = toDateFormat(calendarCommand.getDayId());
        User user = retrieveUser();
        return new UserRecipeCalendar(date,recipe,mealType,user);
    }

    private User retrieveUser() throws Exception {
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

    private Recipe validateToRecipe(Long recipeId) {
        Recipe recipe = null;
        if(recipeId!=null && recipeId > 0L){
            recipe = getRecipeByIdRepository.execute(recipeId);
        }
        if(recipe==null){
            throw new RuntimeException("Invalid recipe id");
        }
        return recipe;
    }

    private MealType validateMealtype(int mealtypeId) {
        MealType mealType = null;
        if(mealtypeId>0){
            //todo cambiar esto a que mealType sea int
            mealType = getMealTypeByIdRepository.execute(Math.toIntExact(mealtypeId));
        }
        if(mealType==null){
            throw new RuntimeException("Invalid mealtype id");
        }
        return mealType;
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
