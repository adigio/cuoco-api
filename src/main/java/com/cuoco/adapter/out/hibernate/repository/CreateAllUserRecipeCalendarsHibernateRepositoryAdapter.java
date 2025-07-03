package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.UserCalendarsHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreateAllUserRecipeCalendarsHibernateRepositoryAdapter extends JpaRepository<UserCalendarsHibernateModel, Long> {}