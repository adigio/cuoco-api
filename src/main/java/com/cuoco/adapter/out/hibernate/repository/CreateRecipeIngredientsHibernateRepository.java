package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.RecipeIngredientsHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreateRecipeIngredientsHibernateRepository extends JpaRepository<RecipeIngredientsHibernateModel, Long> {}
