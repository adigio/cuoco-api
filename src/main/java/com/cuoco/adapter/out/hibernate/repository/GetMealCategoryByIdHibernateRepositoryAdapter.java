package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.MealCategoryHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GetMealCategoryByIdHibernateRepositoryAdapter extends JpaRepository<MealCategoryHibernateModel, Integer> {}
