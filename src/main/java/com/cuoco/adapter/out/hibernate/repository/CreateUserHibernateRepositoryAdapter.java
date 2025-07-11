package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.application.usecase.model.User;
import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreateUserHibernateRepositoryAdapter extends JpaRepository<UserHibernateModel, Long> {
    UserHibernateModel save(User user);
}
