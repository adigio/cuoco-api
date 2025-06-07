package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.DietaryNeedsHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FindDietaryNeedsByNameHibernateRepositoryAdapter extends JpaRepository<DietaryNeedsHibernateModel, Long> {
    List<DietaryNeedsHibernateModel> findByNameIn(List<String> dietaryNeeds);
}
