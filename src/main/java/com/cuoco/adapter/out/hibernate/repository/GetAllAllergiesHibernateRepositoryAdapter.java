package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.AllergyHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GetAllAllergiesHibernateRepositoryAdapter extends JpaRepository<AllergyHibernateModel,Integer> {}
