package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.MealTypeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserRecipesCalendarHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.SaveUserRepositoryCalendarBatchHibernateRepositoryAdapter;
import com.cuoco.application.port.out.SaveUserRecipeCalendarRepository;
import com.cuoco.application.usecase.model.MealType;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserRecipeCalendar;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SaveUserRecipesCalendarRepositoryAdapter implements SaveUserRecipeCalendarRepository {

    private SaveUserRepositoryCalendarBatchHibernateRepositoryAdapter saveUserRepositoryCalendarBatchHibernateRepositoryAdapter;

    public SaveUserRecipesCalendarRepositoryAdapter(SaveUserRepositoryCalendarBatchHibernateRepositoryAdapter saveUserRepositoryCalendarBatchHibernateRepositoryAdapter) {
        this.saveUserRepositoryCalendarBatchHibernateRepositoryAdapter = saveUserRepositoryCalendarBatchHibernateRepositoryAdapter;
    }

    @Override
    public Boolean execute(List<UserRecipeCalendar> userRecipeCalendars) {
        saveUserRepositoryCalendarBatchHibernateRepositoryAdapter.saveAll(buildUserRecipeCalendarHibernateModel(userRecipeCalendars));
        return null;
    }

    private List<UserRecipesCalendarHibernateModel> buildUserRecipeCalendarHibernateModel(List<UserRecipeCalendar> userRecipeCalendars) {
        return userRecipeCalendars.stream()
                .map(urc -> UserRecipesCalendarHibernateModel.builder()
                        .plannedDate(urc.getDate())
                        .recipe(buildRecipe(urc.getRecipe()))
                        .user(buildUser(urc.getUser()))
                        .mealType(buildMealType(urc.getMealType()))
                        .build()
                )
                .collect(Collectors.toList());
    }

    private MealTypeHibernateModel buildMealType(MealType mealType) {
        return MealTypeHibernateModel.builder()
                .id(mealType.getId())
                .build();
    }

    private UserHibernateModel buildUser(User user) {
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
