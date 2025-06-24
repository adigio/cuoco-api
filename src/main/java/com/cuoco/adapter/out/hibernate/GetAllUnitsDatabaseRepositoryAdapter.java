package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.UnitHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllUnitsHibernateRepositoryAdapter;
import com.cuoco.application.port.out.GetAllUnitsRepository;
import com.cuoco.application.usecase.model.Unit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class GetAllUnitsDatabaseRepositoryAdapter implements GetAllUnitsRepository {

    private final GetAllUnitsHibernateRepositoryAdapter getAllUnitsHibernateRepositoryAdapter;

    public GetAllUnitsDatabaseRepositoryAdapter(GetAllUnitsHibernateRepositoryAdapter getAllUnitsHibernateRepositoryAdapter) {
        this.getAllUnitsHibernateRepositoryAdapter = getAllUnitsHibernateRepositoryAdapter;
    }

    @Override
    public List<Unit> execute() {
        log.info("Get all units from database");
        List<UnitHibernateModel> response = getAllUnitsHibernateRepositoryAdapter.findAll();
        return response.stream().map(UnitHibernateModel::toDomain).toList();
    }
}
