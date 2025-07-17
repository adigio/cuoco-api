package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreateUserHibernateRepositoryAdapter extends JpaRepository<UserHibernateModel, Long> {}
