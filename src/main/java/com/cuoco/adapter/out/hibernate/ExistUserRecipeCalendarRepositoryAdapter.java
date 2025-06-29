package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.MealTypeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserRecipesCalendarHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.UserRecipeCalendarExistHibernateRepositoryAdapter;
import com.cuoco.application.port.out.ExistUserRecipeCalendarRepository;
import com.cuoco.application.usecase.model.MealType;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserRecipeCalendar;
import org.springframework.stereotype.Repository;

@Repository
public class ExistUserRecipeCalendarRepositoryAdapter implements ExistUserRecipeCalendarRepository {
    UserRecipeCalendarExistHibernateRepositoryAdapter userRecipeCalendarExistHibernateRepositoryAdapter;

    public ExistUserRecipeCalendarRepositoryAdapter(UserRecipeCalendarExistHibernateRepositoryAdapter userRecipeCalendarExistHibernateRepositoryAdapter) {
        this.userRecipeCalendarExistHibernateRepositoryAdapter = userRecipeCalendarExistHibernateRepositoryAdapter;
    }

    @Override
    public Boolean execute(UserRecipeCalendar userRecipeCalendar) {
        return userRecipeCalendarExistHibernateRepositoryAdapter.existsByUserIdAndRecipeIdAndPlannedDate(userRecipeCalendar.getUser().getId(),
                userRecipeCalendar.getRecipe().getId(), userRecipeCalendar.getDate());
    }



}
