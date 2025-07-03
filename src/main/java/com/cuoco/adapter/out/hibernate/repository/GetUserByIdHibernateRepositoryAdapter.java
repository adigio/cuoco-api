package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GetUserByIdHibernateRepositoryAdapter extends JpaRepository<UserHibernateModel, Long> {}
