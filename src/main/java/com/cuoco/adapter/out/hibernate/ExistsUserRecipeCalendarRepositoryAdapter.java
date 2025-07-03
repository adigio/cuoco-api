package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.repository.ExistsUserRecipeCalendarHibernateRepositoryAdapter;
import com.cuoco.application.port.out.ExistsUserRecipeCalendarRepository;
import com.cuoco.application.usecase.model.Calendar;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class ExistsUserRecipeCalendarRepositoryAdapter implements ExistsUserRecipeCalendarRepository {

    ExistsUserRecipeCalendarHibernateRepositoryAdapter existsUserRecipeCalendarHibernateRepositoryAdapter;

    public ExistsUserRecipeCalendarRepositoryAdapter(
            ExistsUserRecipeCalendarHibernateRepositoryAdapter existsUserRecipeCalendarHibernateRepositoryAdapter
    ) {
        this.existsUserRecipeCalendarHibernateRepositoryAdapter = existsUserRecipeCalendarHibernateRepositoryAdapter;
    }

    @Override
    public Boolean execute(User user, Calendar calendar, Recipe recipe) {
        return existsUserRecipeCalendarHibernateRepositoryAdapter.existsByUserIdAndPlannedDateAndRecipesRecipeId(
                user.getId(),
                calendar.getDate(),
                recipe.getId()
        );
    }
}
