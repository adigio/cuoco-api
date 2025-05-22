package com.cuoco.adapter.out.hibernate;

import com.cuoco.application.port.out.UserExistsByUsernameRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserExistsByUsernameDatabaseRepositoryAdapter implements UserExistsByUsernameRepository {

    private UserExistsByUsernameHibernateRepositoryAdapter userExistsByUsernameHibernateRepositoryAdapter;

    public UserExistsByUsernameDatabaseRepositoryAdapter(UserExistsByUsernameHibernateRepositoryAdapter userExistsByUsernameHibernateRepositoryAdapter) {
        this.userExistsByUsernameHibernateRepositoryAdapter = userExistsByUsernameHibernateRepositoryAdapter;
    }

    @Override
    public Boolean execute(String username) {

        Boolean exists = userExistsByUsernameHibernateRepositoryAdapter.existsByUsername(username);

        return exists;
    }
}
