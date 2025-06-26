package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FindRecipeByNameHibernateRepositoryAdapter extends JpaRepository<RecipeHibernateModel, Long> {
    Optional<RecipeHibernateModel> findByNameIgnoreCase(String name);
}
