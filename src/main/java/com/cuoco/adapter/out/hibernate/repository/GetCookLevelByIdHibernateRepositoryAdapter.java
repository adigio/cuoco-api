package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.CookLevelHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GetCookLevelByIdHibernateRepositoryAdapter extends JpaRepository<CookLevelHibernateModel, Integer> {}
