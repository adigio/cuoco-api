package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GetRecipeByIdHibernateRepositoryAdapter extends JpaRepository<RecipeHibernateModel, Long> {}
