package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.DietHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GetDietByIdHibernateRepositoryAdapter extends JpaRepository<DietHibernateModel, Integer> {}
