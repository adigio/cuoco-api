package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.IngredientHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GetIngredientByNameHibernateRepositoryAdapter extends JpaRepository<IngredientHibernateModel, Long> {
    Optional<IngredientHibernateModel> findByName(String name);
}
