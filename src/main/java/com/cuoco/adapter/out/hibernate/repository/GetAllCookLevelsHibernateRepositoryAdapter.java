package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.CookLevelHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GetAllCookLevelsHibernateRepositoryAdapter extends JpaRepository<CookLevelHibernateModel, Integer> {}
