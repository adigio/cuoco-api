package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.repository.CreateUserMealPrepHibernateRepositoryAdapter;
import com.cuoco.application.usecase.model.MealPrep;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserMealPrep;
import com.cuoco.factory.domain.MealPrepFactory;
import com.cuoco.factory.domain.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateUserMealPrepDatabaseRepositoryAdapterTest {

    @Mock
    private CreateUserMealPrepHibernateRepositoryAdapter createUserMealPrepHibernateRepositoryAdapter;

    private CreateUserMealPrepDatabaseRepositoryAdapter createUserMealPrepDatabaseRepositoryAdapter;

    @BeforeEach
    void setUp() {
        createUserMealPrepDatabaseRepositoryAdapter = new CreateUserMealPrepDatabaseRepositoryAdapter(
                createUserMealPrepHibernateRepositoryAdapter
        );
    }

    @Test
    void GIVEN_valid_user_meal_prep_WHEN_execute_THEN_should_persist_user_meal_preps() {
        User user = UserFactory.create();
        MealPrep mealPrep = MealPrepFactory.create();
        UserMealPrep userMealPrep = UserMealPrep.builder()
                .user(user)
                .mealPrep(mealPrep)
                .build();

        createUserMealPrepDatabaseRepositoryAdapter.execute(userMealPrep);

        verify(createUserMealPrepHibernateRepositoryAdapter).save(any());
    }
} 