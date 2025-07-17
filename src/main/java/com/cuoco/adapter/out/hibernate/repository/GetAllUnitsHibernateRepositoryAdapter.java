package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.UnitHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GetAllUnitsHibernateRepositoryAdapter extends JpaRepository<UnitHibernateModel, Long> {}
