package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.MealTypeHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GetMealTypeByIdHibernateRepositoryAdapter extends JpaRepository<MealTypeHibernateModel, Integer> {}
