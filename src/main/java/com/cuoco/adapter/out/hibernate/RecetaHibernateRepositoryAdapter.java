package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecetaHibernateRepositoryAdapter extends JpaRepository<RecipeHibernateModel, Long> {
}