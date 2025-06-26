package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.UserRecipesHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExistUserRecipesHibernateRepositoryAdapter extends JpaRepository<UserRecipesHibernateModel, Long> {
    boolean existsByUserIdAndRecipeId(Long userId, Long recipeId);
}
