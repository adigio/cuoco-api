package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.PreparationTimeHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface GetPreparationTimeByIdHibernateRepositoryAdapter extends JpaRepository<PreparationTimeHibernateModel, Integer> {}
