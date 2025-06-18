package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.DietHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllDietsHibernateRepositoryAdapter;
import com.cuoco.application.port.out.GetAllDietsRepository;
import com.cuoco.application.usecase.model.Diet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class GetAllDietsDatabaseRepositoryAdapter implements GetAllDietsRepository {

    private final GetAllDietsHibernateRepositoryAdapter getAllDietsHibernateRepositoryAdapter;

    public GetAllDietsDatabaseRepositoryAdapter(GetAllDietsHibernateRepositoryAdapter getAllDietsHibernateRepositoryAdapter) {
        this.getAllDietsHibernateRepositoryAdapter = getAllDietsHibernateRepositoryAdapter;
    }

    @Override
    public List<Diet> execute() {
        log.info("Get all diets from database");

        List<DietHibernateModel> response = getAllDietsHibernateRepositoryAdapter.findAll();

        return response.stream().map(DietHibernateModel::toDomain).toList();
    }
}
