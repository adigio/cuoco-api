package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.UserRecipesHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GetAllUserRecipesByUserIdHibernateRepositoryAdapter extends JpaRepository<UserRecipesHibernateModel, Long> {
    List<UserRecipesHibernateModel> findByUserId(Long userId);
}
