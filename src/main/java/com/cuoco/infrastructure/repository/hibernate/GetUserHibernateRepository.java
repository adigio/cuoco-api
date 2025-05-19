package com.cuoco.infrastructure.repository.hibernate;

import com.cuoco.infrastructure.repository.hibernate.model.UserHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GetUserHibernateRepository extends JpaRepository<UserHibernateModel, Long> {
    Optional<UserHibernateModel> findByUsername(String username);
}