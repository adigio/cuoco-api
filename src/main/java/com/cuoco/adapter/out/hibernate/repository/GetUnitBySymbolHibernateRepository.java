package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.UnitHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GetUnitBySymbolHibernateRepository extends JpaRepository<UnitHibernateModel, Long> {
    Optional<UnitHibernateModel> findBySymbolEqualsIgnoreCase(String symbol);
}
