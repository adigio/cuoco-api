package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.exception.NotFoundException;
import com.cuoco.adapter.out.hibernate.repository.GetUserHibernateRepositoryAdapter;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.port.out.GetUserByEmailRepository;
import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import com.cuoco.shared.model.ErrorDescription;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class GetUserByEmailDatabaseRepositoryAdapter implements GetUserByEmailRepository {

    private final GetUserHibernateRepositoryAdapter getUserHibernateRepositoryAdapter;

    public GetUserByEmailDatabaseRepositoryAdapter(GetUserHibernateRepositoryAdapter getUserHibernateRepositoryAdapter) {
        this.getUserHibernateRepositoryAdapter = getUserHibernateRepositoryAdapter;
    }

    @Override
    public User execute(String email) {

        Optional<UserHibernateModel> userResult = getUserHibernateRepositoryAdapter.findByEmail(email);

        if (userResult.isPresent()) {
            return userResult.get().toDomain();
        } else throw new NotFoundException(ErrorDescription.USER_NOT_EXISTS.getValue());

    }
}
