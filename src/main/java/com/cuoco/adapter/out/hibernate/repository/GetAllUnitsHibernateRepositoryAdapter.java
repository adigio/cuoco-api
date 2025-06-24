package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.UnitHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GetAllUnitsHibernateRepositoryAdapter extends JpaRepository<UnitHibernateModel, Long> {}
