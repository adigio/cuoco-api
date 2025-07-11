package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.PlanHibernateModel;
import com.cuoco.application.usecase.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GetAllPlansHibernateRepository extends JpaRepository<PlanHibernateModel, Long> {}
