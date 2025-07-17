package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FindRecipesByFiltersHibernateRepositoryAdapter extends JpaRepository<RecipeHibernateModel, Long> {

    @Query("SELECT r FROM recipes r WHERE r.id IN :ids")
    List<RecipeHibernateModel> execute(@Param("ids") List<Long> ids);
}
