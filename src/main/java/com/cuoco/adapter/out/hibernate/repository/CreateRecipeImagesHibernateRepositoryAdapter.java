package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.RecipeStepsHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreateRecipeImagesHibernateRepositoryAdapter extends JpaRepository<RecipeStepsHibernateModel, Long> {}
