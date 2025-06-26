package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface FindUserByEmailHibernateRepositoryAdapter extends JpaRepository<UserHibernateModel, Long> {
    Optional<UserHibernateModel> findByEmail(String email);
}