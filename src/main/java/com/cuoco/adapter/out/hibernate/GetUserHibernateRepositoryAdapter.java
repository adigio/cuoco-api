package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GetUserHibernateRepositoryAdapter extends JpaRepository<UserHibernateModel, Long> {
    Optional<UserHibernateModel> findByEmail(String email);
}