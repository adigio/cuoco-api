package com.cuoco.infrastructure.repository.hibernate;

import com.cuoco.infrastructure.repository.hibernate.model.IngredientHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredienteHibernateRepository extends JpaRepository<IngredientHibernateModel, Long> {
    Optional<IngredientHibernateModel> findByNombre(String nombre);
}
