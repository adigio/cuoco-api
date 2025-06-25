package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.RecipeImagesHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreateRecipeImagesHibernateRepositoryAdapter extends JpaRepository<RecipeImagesHibernateModel, Long> {}
