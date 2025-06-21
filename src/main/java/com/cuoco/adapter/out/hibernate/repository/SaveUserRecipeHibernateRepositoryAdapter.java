package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.UserRecipesHibernateModel;
import com.cuoco.application.usecase.model.UserRecipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaveUserRecipeHibernateRepositoryAdapter extends JpaRepository<UserRecipesHibernateModel, Long> {

}
