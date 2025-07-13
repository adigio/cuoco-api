package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.MealTypeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserCalendarRecipesHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserCalendarsHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.DeleteAllUserRecipeCalendarsHibernateRepositoryAdapter;
import com.cuoco.application.port.out.DeleteUserCalendarRepository;
import com.cuoco.application.usecase.model.Calendar;
import com.cuoco.application.usecase.model.CalendarRecipe;
import com.cuoco.application.usecase.model.MealType;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserCalendar;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Repository
@Transactional
@RequiredArgsConstructor
public class DeleteUserCalendarDatabaseRepositoryAdapter implements DeleteUserCalendarRepository {

    private final DeleteAllUserRecipeCalendarsHibernateRepositoryAdapter deleteAllUserRecipeCalendarsHibernateRepositoryAdapter;

    @Override
    public void execute(UserCalendar userCalendar) {
        List<UserCalendarsHibernateModel> userCalendars = userCalendar.getCalendars().stream()
                .map(calendar -> buildUserRecipeCalendarModel(userCalendar.getUser(), calendar))
                .toList();

        deleteAllUserRecipeCalendarsHibernateRepositoryAdapter.deleteAll(userCalendars);
    }

    private UserCalendarsHibernateModel buildUserRecipeCalendarModel(User user, Calendar calendar) {
        UserCalendarsHibernateModel userCalendar = UserCalendarsHibernateModel.builder()
                .id(calendar.getId())
                .user(UserHibernateModel.builder().id(user.getId()).build())
                .plannedDate(calendar.getDate())
                .build();

        userCalendar.setRecipes(calendar.getRecipes().stream().map(recipe -> buildCalendarRecipeModel(userCalendar, recipe)).toList());

        return userCalendar;
    }

    private UserCalendarRecipesHibernateModel buildCalendarRecipeModel(UserCalendarsHibernateModel calendar, CalendarRecipe calendarRecipe) {
        return UserCalendarRecipesHibernateModel.builder()
                .calendar(calendar)
                .recipe(RecipeHibernateModel.builder().id(calendarRecipe.getRecipe().getId()).build())
                .mealType(MealTypeHibernateModel.builder().id(calendarRecipe.getMealType().getId()).build())
                .build();
    }
}
