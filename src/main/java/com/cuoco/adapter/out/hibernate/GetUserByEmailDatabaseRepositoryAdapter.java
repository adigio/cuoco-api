package com.cuoco.adapter.out.hibernate;

import com.cuoco.application.usecase.model.User;
import com.cuoco.application.port.out.GetUserByEmailRepository;
import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class GetUserByEmailDatabaseRepositoryAdapter implements GetUserByEmailRepository {

    private final GetUserHibernateRepositoryAdapter getUserHibernateRepositoryAdapter;

    public GetUserByEmailDatabaseRepositoryAdapter(GetUserHibernateRepositoryAdapter getUserHibernateRepositoryAdapter) {
        this.getUserHibernateRepositoryAdapter = getUserHibernateRepositoryAdapter;
    }

    @Override
    public User execute(String email) throws UsernameNotFoundException {

        UserHibernateModel userResult = getUserHibernateRepositoryAdapter.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return userResult.toDomain();
    }
}
