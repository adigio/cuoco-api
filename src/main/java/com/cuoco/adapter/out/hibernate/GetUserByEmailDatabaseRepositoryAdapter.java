package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.exception.ForbiddenException;
import com.cuoco.adapter.exception.UnprocessableException;
import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserPreferencesHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetUserByEmailHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.GetUserPreferencesByUserIdHibernateRepositoryAdapter;
import com.cuoco.application.port.out.GetUserByEmailRepository;
import com.cuoco.application.usecase.model.User;
import com.cuoco.shared.model.ErrorDescription;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public class GetUserByEmailDatabaseRepositoryAdapter implements GetUserByEmailRepository {

    private final GetUserByEmailHibernateRepositoryAdapter getUserByEmailHibernateRepositoryAdapter;
    private final GetUserPreferencesByUserIdHibernateRepositoryAdapter getUserPreferencesByUserIdHibernateRepositoryAdapter;

    public GetUserByEmailDatabaseRepositoryAdapter(
            GetUserByEmailHibernateRepositoryAdapter getUserByEmailHibernateRepositoryAdapter,
            GetUserPreferencesByUserIdHibernateRepositoryAdapter getUserPreferencesByUserIdHibernateRepositoryAdapter
    ) {
        this.getUserByEmailHibernateRepositoryAdapter = getUserByEmailHibernateRepositoryAdapter;
        this.getUserPreferencesByUserIdHibernateRepositoryAdapter = getUserPreferencesByUserIdHibernateRepositoryAdapter;
    }

    @Override
    public User execute(String email) {

        Optional<UserHibernateModel> userResult = getUserByEmailHibernateRepositoryAdapter.findByEmail(email);

        if (userResult.isPresent()) {

            Optional<UserPreferencesHibernateModel> userPreferencesResult = getUserPreferencesByUserIdHibernateRepositoryAdapter.getByUserId(userResult.get().getId());

            if(userPreferencesResult.isPresent()) {

                User user = userResult.get().toDomain();
                user.setPreferences(userPreferencesResult.get().toDomain());
                return user;

            } else throw new UnprocessableException(ErrorDescription.UNEXPECTED_ERROR.getValue());
        } else throw new ForbiddenException(ErrorDescription.INVALID_CREDENTIALS.getValue());
    }
}
