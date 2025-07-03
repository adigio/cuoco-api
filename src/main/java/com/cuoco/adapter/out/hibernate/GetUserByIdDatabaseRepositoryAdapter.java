package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.exception.UnprocessableException;
import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserPreferencesHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetUserByIdHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.GetUserPreferencesByUserIdHibernateRepositoryAdapter;
import com.cuoco.application.port.out.GetUserByIdRepository;
import com.cuoco.application.usecase.model.User;
import com.cuoco.shared.model.ErrorDescription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
public class GetUserByIdDatabaseRepositoryAdapter implements GetUserByIdRepository {

    private final GetUserByIdHibernateRepositoryAdapter getUserByIdHibernateRepositoryAdapter;
    private final GetUserPreferencesByUserIdHibernateRepositoryAdapter getUserPreferencesByUserIdHibernateRepositoryAdapter;

    public GetUserByIdDatabaseRepositoryAdapter(
            GetUserByIdHibernateRepositoryAdapter getUserByIdHibernateRepositoryAdapter,
            GetUserPreferencesByUserIdHibernateRepositoryAdapter getUserPreferencesByUserIdHibernateRepositoryAdapter
    ) {
        this.getUserByIdHibernateRepositoryAdapter = getUserByIdHibernateRepositoryAdapter;
        this.getUserPreferencesByUserIdHibernateRepositoryAdapter = getUserPreferencesByUserIdHibernateRepositoryAdapter;
    }

    @Override
    public User execute(Long id) {
        Optional<UserHibernateModel> userResult = getUserByIdHibernateRepositoryAdapter.findById(id);

        if (userResult.isEmpty()) {
            throw new UnprocessableException(ErrorDescription.UNEXPECTED_ERROR.getValue());
        }

        Optional<UserPreferencesHibernateModel> userPreferencesResult = getUserPreferencesByUserIdHibernateRepositoryAdapter.getByUserId(userResult.get().getId());

        if(userPreferencesResult.isEmpty()) {
            throw new UnprocessableException(ErrorDescription.UNEXPECTED_ERROR.getValue());
        }

        User user = userResult.get().toDomain();
        user.setPreferences(userPreferencesResult.get().toDomain());

        return user;
    }
}
