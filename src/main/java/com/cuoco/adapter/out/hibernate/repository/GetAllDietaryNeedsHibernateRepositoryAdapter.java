package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.DietaryNeedHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GetAllDietaryNeedsHibernateRepositoryAdapter extends JpaRepository<DietaryNeedHibernateModel, Integer> {}
