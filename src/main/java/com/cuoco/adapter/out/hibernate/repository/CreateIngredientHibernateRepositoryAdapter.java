package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.IngredientHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreateIngredientHibernateRepositoryAdapter extends JpaRepository<IngredientHibernateModel, Long> {}
