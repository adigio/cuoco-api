package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.UnitHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GetUnitBySymbolHibernateRepositoryAdapter extends JpaRepository<UnitHibernateModel, Long> {
    Optional<UnitHibernateModel> findBySymbolEqualsIgnoreCase(String symbol);
}
