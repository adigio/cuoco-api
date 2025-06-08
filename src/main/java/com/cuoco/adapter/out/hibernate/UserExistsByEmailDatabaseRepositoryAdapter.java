package com.cuoco.adapter.out.hibernate;


import com.cuoco.adapter.out.hibernate.repository.UserExistsByEmailHibernateRepositoryAdapter;
import com.cuoco.application.port.out.UserExistsByEmailRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserExistsByEmailDatabaseRepositoryAdapter implements UserExistsByEmailRepository {

    private UserExistsByEmailHibernateRepositoryAdapter userExistsByEmailHibernateRepositoryAdapter;

    public UserExistsByEmailDatabaseRepositoryAdapter(UserExistsByEmailHibernateRepositoryAdapter userExistsByEmailHibernateRepositoryAdapter) {
        this.userExistsByEmailHibernateRepositoryAdapter = userExistsByEmailHibernateRepositoryAdapter;
    }

    @Override
    public Boolean execute(String email) {

        Boolean exists = userExistsByEmailHibernateRepositoryAdapter.existsByEmail(email);

        return exists;
    }
}
