package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.UserRecipesHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreateUserRecipeHibernateRepositoryAdapter extends JpaRepository<UserRecipesHibernateModel, Long> {}
