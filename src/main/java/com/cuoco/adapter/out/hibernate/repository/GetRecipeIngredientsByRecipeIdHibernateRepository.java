package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.RecipeIngredientsHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GetRecipeIngredientsByRecipeIdHibernateRepository extends JpaRepository<RecipeIngredientsHibernateModel, Long> {

    List<RecipeIngredientsHibernateModel> findByRecipeId(Long id);
}
