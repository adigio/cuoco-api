package com.cuoco.infrastructure.repository.database;

import com.cuoco.domain.model.User;
import com.cuoco.domain.port.repository.GetUserByUsernameRepository;
import com.cuoco.infrastructure.repository.hibernate.GetUserHibernateRepository;
import com.cuoco.infrastructure.repository.hibernate.model.UserHibernateModel;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class GetUserByUsernameDatabaseRepository implements GetUserByUsernameRepository {

    private GetUserHibernateRepository getUserDatabaseRepository;

    public GetUserByUsernameDatabaseRepository(GetUserHibernateRepository getUserDatabaseRepository) {
        this.getUserDatabaseRepository = getUserDatabaseRepository;
    }

    public User execute(String username) {
        UserHibernateModel userDatabase = getUserDatabaseRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));;

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
