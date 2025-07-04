package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.repository.ExistsUserMealPrepByUserIdAndMealPrepIdHibernateRepositoryAdapter;
import com.cuoco.application.port.out.ExistsUserMealPrepRepository;
import com.cuoco.application.usecase.model.UserMealPrep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class ExistsUserMealPrepDatabaseRepositoryAdapter implements ExistsUserMealPrepRepository {

    private final ExistsUserMealPrepByUserIdAndMealPrepIdHibernateRepositoryAdapter existsUserMealPrepByUserIdAndMealPrepIdHibernateRepositoryAdapter;

    public ExistsUserMealPrepDatabaseRepositoryAdapter(ExistsUserMealPrepByUserIdAndMealPrepIdHibernateRepositoryAdapter existsUserMealPrepByUserIdAndMealPrepIdHibernateRepositoryAdapter) {
        this.existsUserMealPrepByUserIdAndMealPrepIdHibernateRepositoryAdapter = existsUserMealPrepByUserIdAndMealPrepIdHibernateRepositoryAdapter;
    }

    @Override
    public boolean execute(UserMealPrep userMealPrep) {
        return existsUserMealPrepByUserIdAndMealPrepIdHibernateRepositoryAdapter.existsByUserIdAndMealPrepId(userMealPrep.getUser().getId(), userMealPrep.getMealPrep().getId());
    }
}
