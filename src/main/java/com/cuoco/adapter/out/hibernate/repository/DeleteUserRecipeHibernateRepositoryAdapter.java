package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.UserRecipesHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeleteUserRecipeHibernateRepositoryAdapter extends JpaRepository<UserRecipesHibernateModel, Long> {
    void deleteAllByUserIdAndRecipeId(Long userId, Long recipeId);
}
