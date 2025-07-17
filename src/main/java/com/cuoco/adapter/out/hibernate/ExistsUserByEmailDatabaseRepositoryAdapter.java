package com.cuoco.adapter.out.hibernate;


import com.cuoco.adapter.out.hibernate.repository.ExistsUserByEmailHibernateRepositoryAdapter;
import com.cuoco.application.port.out.ExistsUserByEmailRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ExistsUserByEmailDatabaseRepositoryAdapter implements ExistsUserByEmailRepository {

    private final ExistsUserByEmailHibernateRepositoryAdapter existsUserByEmailHibernateRepositoryAdapter;

    public ExistsUserByEmailDatabaseRepositoryAdapter(ExistsUserByEmailHibernateRepositoryAdapter existsUserByEmailHibernateRepositoryAdapter) {
        this.existsUserByEmailHibernateRepositoryAdapter = existsUserByEmailHibernateRepositoryAdapter;
    }

    @Override
    public Boolean execute(String email) {
        return existsUserByEmailHibernateRepositoryAdapter.existsByEmail(email);
    }
}
