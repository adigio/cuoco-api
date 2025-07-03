package com.cuoco.adapter.out.hibernate;


import com.cuoco.adapter.out.hibernate.repository.UserExistsByEmailHibernateRepositoryAdapter;
import com.cuoco.application.port.out.ExistsUserByEmailRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ExistsUserByEmailDatabaseRepositoryAdapter implements ExistsUserByEmailRepository {

    private final UserExistsByEmailHibernateRepositoryAdapter userExistsByEmailHibernateRepositoryAdapter;

    public ExistsUserByEmailDatabaseRepositoryAdapter(UserExistsByEmailHibernateRepositoryAdapter userExistsByEmailHibernateRepositoryAdapter) {
        this.userExistsByEmailHibernateRepositoryAdapter = userExistsByEmailHibernateRepositoryAdapter;
    }

    @Override
    public Boolean execute(String email) {
        return userExistsByEmailHibernateRepositoryAdapter.existsByEmail(email);
    }
}
