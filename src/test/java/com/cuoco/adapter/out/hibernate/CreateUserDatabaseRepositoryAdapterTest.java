package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.CreateUserDatabaseRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserPreferencesHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.CreateUserHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.CreateUserPreferencesHibernateRepositoryAdapter;
import com.cuoco.application.usecase.model.User;
import com.cuoco.factory.domain.UserFactory;
import com.cuoco.factory.hibernate.UserHibernateModelFactory;
import com.cuoco.factory.hibernate.UserPreferencesHibernateModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class CreateUserDatabaseRepositoryAdapterTest {

    @Mock
    private CreateUserHibernateRepositoryAdapter userRepository;

    @Mock
    private CreateUserPreferencesHibernateRepositoryAdapter preferencesRepository;

    @InjectMocks
    private CreateUserDatabaseRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void GIVEN_valid_user_WHEN_execute_THEN_should_persist_user_preferences_dietaryNeeds_allergies() {
        User domainUser = UserFactory.create();

        UserHibernateModel savedUser = UserHibernateModelFactory.create();
        savedUser.setId(1L);

        UserPreferencesHibernateModel savedPreferences = UserPreferencesHibernateModelFactory.create();

        when(userRepository.save(any(UserHibernateModel.class))).thenReturn(savedUser);
        when(preferencesRepository.save(any())).thenReturn(savedPreferences);

        User result = adapter.execute(domainUser);

        assertNotNull(result);
        assertEquals(savedUser.getEmail(), result.getEmail());
        assertEquals(savedPreferences.getDiet().getDescription(), result.getPreferences().getDiet().getDescription());
        assertEquals(3, result.getAllergies().size());
        assertEquals(3, result.getDietaryNeeds().size());

        verify(userRepository).save(any(UserHibernateModel.class));
        verify(preferencesRepository).save(any());
    }
}
