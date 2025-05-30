package com.cuoco.adapter.out.hibernate;

import com.cuoco.application.usecase.model.User;
import com.cuoco.application.port.out.GetUserByUsernameRepository;
import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class GetUserByUsernameDatabaseRepositoryAdapter implements GetUserByUsernameRepository {

    private final GetUserHibernateRepositoryAdapter getUserHibernateRepositoryAdapter;

    public GetUserByUsernameDatabaseRepositoryAdapter(GetUserHibernateRepositoryAdapter getUserHibernateRepositoryAdapter) {
        this.getUserHibernateRepositoryAdapter = getUserHibernateRepositoryAdapter;
    }

    @Override
    public User execute(String username) throws UsernameNotFoundException {

        UserHibernateModel userResult = getUserHibernateRepositoryAdapter.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return userResult.toDomain();
    }
}
