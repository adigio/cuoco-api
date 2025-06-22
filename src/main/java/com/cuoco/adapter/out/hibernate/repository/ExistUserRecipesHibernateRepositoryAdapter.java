package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserRecipesHibernateModel;
import com.cuoco.application.usecase.model.UserRecipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExistUserRecipesHibernateRepositoryAdapter extends JpaRepository<UserRecipesHibernateModel, Long> {
    boolean existsByUserIdAndRecipeId(Long userId, Long recipeId);

}
