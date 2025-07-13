package com.cuoco.adapter.out.hibernate;

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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserDatabaseRepositoryAdapterTest {

    @Mock
    private CreateUserHibernateRepositoryAdapter createUserHibernateRepositoryAdapter;
    @Mock
    private CreateUserPreferencesHibernateRepositoryAdapter createUserPreferencesHibernateRepositoryAdapter;

    private UpdateUserDatabaseRepositoryAdapter updateUserDatabaseRepositoryAdapter;

    @BeforeEach
    void setUp() {
        updateUserDatabaseRepositoryAdapter = new UpdateUserDatabaseRepositoryAdapter(
                createUserHibernateRepositoryAdapter,
                createUserPreferencesHibernateRepositoryAdapter
        );
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        User userToUpdate = UserFactory.create();
        userToUpdate.setEmail("test@example.com");
        userToUpdate.setName("Updated Name");

        UserHibernateModel savedUser = UserHibernateModelFactory.create();
        savedUser.setName("Updated Name");
        
        UserPreferencesHibernateModel savedPreferences = UserPreferencesHibernateModelFactory.create();

        when(createUserHibernateRepositoryAdapter.save(any(UserHibernateModel.class)))
                .thenReturn(savedUser);
        when(createUserPreferencesHibernateRepositoryAdapter.save(any(UserPreferencesHibernateModel.class)))
                .thenReturn(savedPreferences);

        User result = updateUserDatabaseRepositoryAdapter.execute(userToUpdate);

        assertNotNull(result);
        verify(createUserHibernateRepositoryAdapter, times(1)).save(any(UserHibernateModel.class));
        verify(createUserPreferencesHibernateRepositoryAdapter, times(1)).save(any(UserPreferencesHibernateModel.class));
    }

    @Test
    void shouldUpdateUserWithAllFields() {
        User userToUpdate = UserFactory.create();
        userToUpdate.setEmail("test@example.com");

        UserHibernateModel savedUser = UserHibernateModelFactory.create();
        UserPreferencesHibernateModel savedPreferences = UserPreferencesHibernateModelFactory.create();

        when(createUserHibernateRepositoryAdapter.save(any(UserHibernateModel.class)))
                .thenReturn(savedUser);
        when(createUserPreferencesHibernateRepositoryAdapter.save(any(UserPreferencesHibernateModel.class)))
                .thenReturn(savedPreferences);

        User result = updateUserDatabaseRepositoryAdapter.execute(userToUpdate);

        assertNotNull(result);
        verify(createUserHibernateRepositoryAdapter, times(1)).save(any(UserHibernateModel.class));
        verify(createUserPreferencesHibernateRepositoryAdapter, times(1)).save(any(UserPreferencesHibernateModel.class));
    }

    @Test
    void shouldHandleUserWithNullFields() {
        User userToUpdate = UserFactory.create();
        userToUpdate.setEmail("test@example.com");
        userToUpdate.setName(null);

        UserHibernateModel savedUser = UserHibernateModelFactory.create();
        UserPreferencesHibernateModel savedPreferences = UserPreferencesHibernateModelFactory.create();

        when(createUserHibernateRepositoryAdapter.save(any(UserHibernateModel.class)))
                .thenReturn(savedUser);
        when(createUserPreferencesHibernateRepositoryAdapter.save(any(UserPreferencesHibernateModel.class)))
                .thenReturn(savedPreferences);

        User result = updateUserDatabaseRepositoryAdapter.execute(userToUpdate);

        assertNotNull(result);
        verify(createUserHibernateRepositoryAdapter, times(1)).save(any(UserHibernateModel.class));
        verify(createUserPreferencesHibernateRepositoryAdapter, times(1)).save(any(UserPreferencesHibernateModel.class));
    }
}