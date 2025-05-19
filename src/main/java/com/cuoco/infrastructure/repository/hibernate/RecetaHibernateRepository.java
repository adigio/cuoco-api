package com.cuoco.infrastructure.repository.hibernate;

import com.cuoco.infrastructure.repository.hibernate.model.RecipeHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecetaHibernateRepository extends JpaRepository<RecipeHibernateModel, Long> {
}