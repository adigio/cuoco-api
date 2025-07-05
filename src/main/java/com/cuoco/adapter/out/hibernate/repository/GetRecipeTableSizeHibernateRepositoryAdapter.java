package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import com.cuoco.application.usecase.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GetRecipeTableSizeHibernateRepositoryAdapter extends JpaRepository<RecipeHibernateModel,Long> {
}
