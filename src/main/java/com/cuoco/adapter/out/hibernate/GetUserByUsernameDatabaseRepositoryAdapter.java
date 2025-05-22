package com.cuoco.adapter.out.hibernate;

import com.cuoco.application.usecase.model.User;
import com.cuoco.application.port.out.GetUserByUsernameRepository;
import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class GetUserByUsernameDatabaseRepositoryAdapter implements GetUserByUsernameRepository {

    private GetUserHibernateRepositoryAdapter getUserDatabaseRepository;

    public GetUserByUsernameDatabaseRepositoryAdapter(GetUserHibernateRepositoryAdapter getUserDatabaseRepository) {
        this.getUserDatabaseRepository = getUserDatabaseRepository;
    }

    public User execute(String username) {
        UserHibernateModel userDatabase = getUserDatabaseRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return new User(
                null,
                null,
                null,
                userDatabase.getUsername(),
                userDatabase.getPassword(),
                null
        );
    }
}
