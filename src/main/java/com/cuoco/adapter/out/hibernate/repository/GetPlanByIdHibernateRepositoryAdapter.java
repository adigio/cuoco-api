package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.PlanHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GetPlanByIdHibernateRepositoryAdapter extends JpaRepository<PlanHibernateModel, Integer> {}
