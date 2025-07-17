package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GetRecipesMaxIdHibernateRepositoryAdapter extends JpaRepository<RecipeHibernateModel, Long> {

    @Query("SELECT MAX(r.id) FROM recipes r")
    Long execute();
}
