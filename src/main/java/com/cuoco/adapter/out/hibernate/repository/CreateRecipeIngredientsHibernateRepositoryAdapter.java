package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.RecipeIngredientsHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreateRecipeIngredientsHibernateRepositoryAdapter extends JpaRepository<RecipeIngredientsHibernateModel, Long> {}
