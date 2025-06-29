package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.MealTypeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserRecipesCalendarHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllUserRecipesCalendarHibernateRepositoryAdapter;
import com.cuoco.application.port.out.GetUserRecipeCalendarRepository;
import com.cuoco.application.usecase.model.UserRecipeCalendar;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class GetAllUserRecipesCalendarByUserRepositoryAdapter implements GetUserRecipeCalendarRepository {

    private GetAllUserRecipesCalendarHibernateRepositoryAdapter getAllUserRecipesCalendarHibernateRepositoryAdapter;

    public GetAllUserRecipesCalendarByUserRepositoryAdapter(GetAllUserRecipesCalendarHibernateRepositoryAdapter getAllUserRecipesCalendarHibernateRepositoryAdapter) {
        this.getAllUserRecipesCalendarHibernateRepositoryAdapter = getAllUserRecipesCalendarHibernateRepositoryAdapter;
    }

    @Override
    public List<UserRecipeCalendar> execute(Long id) {
        List<UserRecipesCalendarHibernateModel> response = getAllUserRecipesCalendarHibernateRepositoryAdapter.findAllByUserId(id);
        return response.stream().map(UserRecipesCalendarHibernateModel::toDomain).toList();
    }
}
