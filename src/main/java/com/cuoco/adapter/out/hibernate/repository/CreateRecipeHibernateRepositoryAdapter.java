package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreateRecipeHibernateRepositoryAdapter extends JpaRepository<RecipeHibernateModel, Long> {
    
    Optional<RecipeHibernateModel> findByNameIgnoreCase(String name);
}
