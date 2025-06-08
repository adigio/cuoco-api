package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.IngredientHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IngredienteHibernateRepositoryAdapter extends JpaRepository<IngredientHibernateModel, Long> {
    Optional<IngredientHibernateModel> findByName(String name);
}
