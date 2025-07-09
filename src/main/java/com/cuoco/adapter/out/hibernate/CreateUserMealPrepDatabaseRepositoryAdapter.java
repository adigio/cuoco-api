package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.MealPrepHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserMealPrepHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.CreateUserMealPrepHibernateRepositoryAdapter;
import com.cuoco.application.port.out.CreateUserMealPrepRepository;
import com.cuoco.application.usecase.model.UserMealPrep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class CreateUserMealPrepDatabaseRepositoryAdapter implements CreateUserMealPrepRepository {

    private final CreateUserMealPrepHibernateRepositoryAdapter createUserMealPrepHibernateRepositoryAdapter;

    public CreateUserMealPrepDatabaseRepositoryAdapter(CreateUserMealPrepHibernateRepositoryAdapter createUserMealPrepHibernateRepositoryAdapter) {
        this.createUserMealPrepHibernateRepositoryAdapter = createUserMealPrepHibernateRepositoryAdapter;
    }

    @Override
    public void execute(UserMealPrep userMealPrep) {
        log.info("Executing create user meal prep in database with body {}", userMealPrep);
        createUserMealPrepHibernateRepositoryAdapter.save(buildUserMealPrepModel(userMealPrep));
    }

    private UserMealPrepHibernateModel buildUserMealPrepModel(UserMealPrep userMealPrep) {
        return UserMealPrepHibernateModel
                .builder()
                .user(UserHibernateModel.builder().id(userMealPrep.getUser().getId()).build())
                .mealPrep(MealPrepHibernateModel.builder().id(userMealPrep.getMealPrep().getId()).build())
                .build();
    }
}
