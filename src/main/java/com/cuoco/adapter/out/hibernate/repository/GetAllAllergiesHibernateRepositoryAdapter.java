package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.AllergyHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GetAllAllergiesHibernateRepositoryAdapter extends JpaRepository<AllergyHibernateModel,Integer> {}
