package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.MealPrepHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GetAllMealPrepsByIdsHibernateRepositoryAdapter extends JpaRepository<MealPrepHibernateModel, Long> {}
