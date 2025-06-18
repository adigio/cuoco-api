package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.exception.ForbiddenException;
import com.cuoco.adapter.exception.UnprocessableException;
import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserPreferencesHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.FindUserByEmailHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.FindUserPreferencesByIdHibernateRepositoryAdapter;
import com.cuoco.application.usecase.model.User;
import com.cuoco.factory.hibernate.UserHibernateModelFactory;
import com.cuoco.factory.hibernate.UserPreferencesHibernateModelFactory;
import com.cuoco.shared.model.ErrorDescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class GetUserByEmailDatabaseRepositoryAdapterTest {

    @Mock
    private FindUserByEmailHibernateRepositoryAdapter userRepository;

    @Mock
    private FindUserPreferencesByIdHibernateRepositoryAdapter preferencesRepository;

    @InjectMocks
    private GetUserByEmailDatabaseRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void WHEN_execute_with_existing_user_and_preferences_THEN_return_user_with_preferences() {
        UserPreferencesHibernateModel preferencesModel = UserPreferencesHibernateModelFactory.create();
        UserHibernateModel userModel = preferencesModel.getUser();
        String email = userModel.getEmail();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userModel));
        when(preferencesRepository.findById(userModel.getId())).thenReturn(Optional.of(preferencesModel));

        User result = adapter.execute(email);

        assertEquals(email, result.getEmail());
        assertNotNull(result.getPreferences());
        verify(userRepository).findByEmail(email);
        verify(preferencesRepository).findById(userModel.getId());
    }

    @Test
    void WHEN_execute_with_existing_user_but_missing_preferences_THEN_throw_UnprocessableException() {
        UserHibernateModel userModel = UserHibernateModelFactory.create();
        String email = userModel.getEmail();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userModel));
        when(preferencesRepository.findById(userModel.getId())).thenReturn(Optional.empty());

        UnprocessableException ex = assertThrows(UnprocessableException.class, () -> adapter.execute(email));
        assertEquals(ErrorDescription.UNEXPECTED_ERROR.getValue(), ex.getDescription());

        verify(userRepository).findByEmail(email);
        verify(preferencesRepository).findById(userModel.getId());
    }

    @Test
    void WHEN_execute_with_non_existing_user_THEN_throw_ForbiddenException() {
        String email = "notfound@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        ForbiddenException ex = assertThrows(ForbiddenException.class, () -> adapter.execute(email));
        assertEquals(ErrorDescription.INVALID_CREDENTIALS.getValue(), ex.getDescription());

        verify(userRepository).findByEmail(email);
        verifyNoInteractions(preferencesRepository);
    }
}
