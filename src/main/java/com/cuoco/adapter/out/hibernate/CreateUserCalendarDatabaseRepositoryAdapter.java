package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.MealTypeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserCalendarRecipesHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserCalendarsHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.CreateAllUserRecipeCalendarsHibernateRepositoryAdapter;
import com.cuoco.application.port.out.CreateUserCalendarRepository;
import com.cuoco.application.usecase.model.Calendar;
import com.cuoco.application.usecase.model.MealType;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.CalendarRecipe;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserCalendar;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CreateUserCalendarDatabaseRepositoryAdapter implements CreateUserCalendarRepository {

    private final CreateAllUserRecipeCalendarsHibernateRepositoryAdapter createAllUserRecipeCalendarsHibernateRepositoryAdapter;

    @Override
    public void execute(UserCalendar userRecipeCalendars) {
        log.info("Executing create calendar for user with id {} in database", userRecipeCalendars.getUser().getId());

        UserHibernateModel user = buildUserModel(userRecipeCalendars.getUser());

        List<UserCalendarsHibernateModel> userCalendars = userRecipeCalendars.getCalendars().stream()
                .map(calendar -> buildUserRecipeCalendarModel(user, calendar))
                .toList();

        createAllUserRecipeCalendarsHibernateRepositoryAdapter.saveAll(userCalendars);
    }

    private UserCalendarsHibernateModel buildUserRecipeCalendarModel(UserHibernateModel user, Calendar calendar) {
        UserCalendarsHibernateModel userCalendar = UserCalendarsHibernateModel.builder()
                .user(user)
                .plannedDate(calendar.getDate())
                .build();

        userCalendar.setRecipes(calendar.getRecipes().stream().map(recipe -> buildCalendarRecipeModel(userCalendar, recipe)).toList());

        return userCalendar;
    }

    private UserCalendarRecipesHibernateModel buildCalendarRecipeModel(UserCalendarsHibernateModel calendar, CalendarRecipe calendarRecipe) {
        return UserCalendarRecipesHibernateModel.builder()
                .calendar(calendar)
                .recipe(buildRecipe(calendarRecipe.getRecipe()))
                .mealType(buildMealTypeModel(calendarRecipe.getMealType()))
                .build();
    }

    private MealTypeHibernateModel buildMealTypeModel(MealType mealType) {
        return MealTypeHibernateModel.builder()
                .id(mealType.getId())
                .build();
    }

    private UserHibernateModel buildUserModel(User user) {
        return UserHibernateModel.builder()
                .id(user.getId())
                .build();
    }

    private RecipeHibernateModel buildRecipe(Recipe recipe) {
        return RecipeHibernateModel.builder()
                .id(recipe.getId())
                .build();
    }
}
