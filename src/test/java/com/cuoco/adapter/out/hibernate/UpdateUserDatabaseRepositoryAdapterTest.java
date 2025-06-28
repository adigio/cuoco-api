package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.UserAllergiesRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.UserDietaryNeedsRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.CreateUserHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.CreateUserPreferencesHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.FindUserByEmailHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.FindUserPreferencesByIdHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.GetDietaryNeedsByIdHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.GetAllergiesByIdHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.GetDietByIdHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.GetCookLevelByIdHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.GetPlanByIdHibernateRepositoryAdapter;
import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.usecase.model.User;
import com.cuoco.factory.domain.UserFactory;
import com.cuoco.factory.hibernate.UserHibernateModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserDatabaseRepositoryAdapterTest {

    @Mock
    private CreateUserHibernateRepositoryAdapter createUserHibernateRepositoryAdapter;
    @Mock
    private CreateUserPreferencesHibernateRepositoryAdapter createUserPreferencesHibernateRepositoryAdapter;
    @Mock
    private UserDietaryNeedsRepositoryAdapter userDietaryNeedsRepositoryAdapter;
    @Mock
    private UserAllergiesRepositoryAdapter userAllergiesRepositoryAdapter;
    @Mock
    private FindUserByEmailHibernateRepositoryAdapter findUserByEmailHibernateRepositoryAdapter;
    @Mock
    private FindUserPreferencesByIdHibernateRepositoryAdapter findUserPreferencesByIdHibernateRepositoryAdapter;
    @Mock
    private GetDietaryNeedsByIdHibernateRepositoryAdapter getDietaryNeedsByIdHibernateRepositoryAdapter;
    @Mock
    private GetAllergiesByIdHibernateRepositoryAdapter getAllergiesByIdHibernateRepositoryAdapter;
    @Mock
    private GetDietByIdHibernateRepositoryAdapter getDietByIdHibernateRepositoryAdapter;
    @Mock
    private GetCookLevelByIdHibernateRepositoryAdapter getCookLevelByIdHibernateRepositoryAdapter;
    @Mock
    private GetPlanByIdHibernateRepositoryAdapter getPlanByIdHibernateRepositoryAdapter;

    private UpdateUserDatabaseRepositoryAdapter updateUserDatabaseRepositoryAdapter;

    @BeforeEach
    void setUp() {
        updateUserDatabaseRepositoryAdapter = new UpdateUserDatabaseRepositoryAdapter(
                createUserHibernateRepositoryAdapter,
                createUserPreferencesHibernateRepositoryAdapter,
                            userDietaryNeedsRepositoryAdapter,
            userAllergiesRepositoryAdapter,
                findUserByEmailHibernateRepositoryAdapter,
                findUserPreferencesByIdHibernateRepositoryAdapter,
                getDietaryNeedsByIdHibernateRepositoryAdapter,
                getAllergiesByIdHibernateRepositoryAdapter,
                getDietByIdHibernateRepositoryAdapter,
                getCookLevelByIdHibernateRepositoryAdapter,
                getPlanByIdHibernateRepositoryAdapter
        );
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        // Given
        User userToUpdate = UserFactory.create();
        userToUpdate.setEmail("test@example.com");
        userToUpdate.setName("Updated Name");

        UserHibernateModel existingUser = UserHibernateModelFactory.create();
        UserHibernateModel savedUser = UserHibernateModelFactory.create();
        savedUser.setName("Updated Name");

        when(findUserByEmailHibernateRepositoryAdapter.findByEmail("test@example.com"))
                .thenReturn(Optional.of(existingUser));
        when(createUserHibernateRepositoryAdapter.save(any(UserHibernateModel.class)))
                .thenReturn(savedUser);

        // When
        User result = updateUserDatabaseRepositoryAdapter.execute(userToUpdate);

        // Then
        assertNotNull(result);
        verify(findUserByEmailHibernateRepositoryAdapter, times(1)).findByEmail("test@example.com");
        verify(createUserHibernateRepositoryAdapter, times(1)).save(any(UserHibernateModel.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        User userToUpdate = UserFactory.create();
        userToUpdate.setEmail("notfound@example.com");

        when(findUserByEmailHibernateRepositoryAdapter.findByEmail("notfound@example.com"))
                .thenReturn(Optional.empty());

        // When & Then
        BadRequestException exception = assertThrows(BadRequestException.class, () -> updateUserDatabaseRepositoryAdapter.execute(userToUpdate));

        assertEquals("El usuario ingresado no existe", exception.getDescription());
        verify(findUserByEmailHibernateRepositoryAdapter, times(1)).findByEmail("notfound@example.com");
        verify(createUserHibernateRepositoryAdapter, never()).save(any(UserHibernateModel.class));
    }

    @Test
    void shouldUpdateUserWithAllFields() {
        // Given
        User userToUpdate = UserFactory.create();
        userToUpdate.setEmail("test@example.com");

        UserHibernateModel existingUser = UserHibernateModelFactory.create();
        UserHibernateModel savedUser = UserHibernateModelFactory.create();

        when(findUserByEmailHibernateRepositoryAdapter.findByEmail("test@example.com"))
                .thenReturn(Optional.of(existingUser));
        when(createUserHibernateRepositoryAdapter.save(any(UserHibernateModel.class)))
                .thenReturn(savedUser);

        // When
        User result = updateUserDatabaseRepositoryAdapter.execute(userToUpdate);

        // Then
        assertNotNull(result);
        verify(findUserByEmailHibernateRepositoryAdapter, times(1)).findByEmail("test@example.com");
        verify(createUserHibernateRepositoryAdapter, times(1)).save(any(UserHibernateModel.class));
    }

    @Test
    void shouldHandleUserWithNullFields() {
        // Given
        User userToUpdate = UserFactory.create();
        userToUpdate.setEmail("test@example.com");
        userToUpdate.setName(null);

        UserHibernateModel existingUser = UserHibernateModelFactory.create();
        UserHibernateModel savedUser = UserHibernateModelFactory.create();

        when(findUserByEmailHibernateRepositoryAdapter.findByEmail("test@example.com"))
                .thenReturn(Optional.of(existingUser));
        when(createUserHibernateRepositoryAdapter.save(any(UserHibernateModel.class)))
                .thenReturn(savedUser);

        // When
        User result = updateUserDatabaseRepositoryAdapter.execute(userToUpdate);

        // Then
        assertNotNull(result);
        verify(findUserByEmailHibernateRepositoryAdapter, times(1)).findByEmail("test@example.com");
        verify(createUserHibernateRepositoryAdapter, times(1)).save(any(UserHibernateModel.class));
    }
} 