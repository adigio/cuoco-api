package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.exception.NotFoundException;
import com.cuoco.adapter.exception.UnprocessableException;
import com.cuoco.adapter.out.hibernate.model.UserPreferencesHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.FindUserByEmailHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.FindUserPreferencesByIdHibernateRepositoryAdapter;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.port.out.GetUserByEmailRepository;
import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import com.cuoco.shared.model.ErrorDescription;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class GetUserByEmailDatabaseRepositoryAdapter implements GetUserByEmailRepository {

    private final FindUserByEmailHibernateRepositoryAdapter findUserByEmailHibernateRepositoryAdapter;
    private final FindUserPreferencesByIdHibernateRepositoryAdapter findUserPreferencesByIdHibernateRepositoryAdapter;

    public GetUserByEmailDatabaseRepositoryAdapter(
            FindUserByEmailHibernateRepositoryAdapter findUserByEmailHibernateRepositoryAdapter,
            FindUserPreferencesByIdHibernateRepositoryAdapter findUserPreferencesByIdHibernateRepositoryAdapter
    ) {
        this.findUserByEmailHibernateRepositoryAdapter = findUserByEmailHibernateRepositoryAdapter;
        this.findUserPreferencesByIdHibernateRepositoryAdapter = findUserPreferencesByIdHibernateRepositoryAdapter;
    }

    @Override
    public User execute(String email) {

        Optional<UserHibernateModel> userResult = findUserByEmailHibernateRepositoryAdapter.findByEmail(email);

        if (userResult.isPresent()) {

            Optional<UserPreferencesHibernateModel> userPreferencesResult = findUserPreferencesByIdHibernateRepositoryAdapter.findById(userResult.get().getId());

            if(userPreferencesResult.isPresent()) {

                User user = userResult.get().toDomain();
                user.setPreferences(userPreferencesResult.get().toDomain());
                return user;

            } else throw new UnprocessableException(ErrorDescription.UNEXPECTED_ERROR.getValue());
        } else throw new NotFoundException(ErrorDescription.USER_NOT_EXISTS.getValue());
    }
}
