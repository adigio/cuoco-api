package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.CookLevelHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllCookLevelsHibernateRepositoryAdapter;
import com.cuoco.application.port.out.GetAllCookLevelsRepository;
import com.cuoco.application.usecase.model.CookLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class GetAllCookLevelsDatabaseRepositoryAdapter implements GetAllCookLevelsRepository {

    private final GetAllCookLevelsHibernateRepositoryAdapter getAllCookLevelsHibernateRepositoryAdapter;

    public GetAllCookLevelsDatabaseRepositoryAdapter(GetAllCookLevelsHibernateRepositoryAdapter getAllCookLevelsHibernateRepositoryAdapter) {
        this.getAllCookLevelsHibernateRepositoryAdapter = getAllCookLevelsHibernateRepositoryAdapter;
    }

    @Override
    public List<CookLevel> execute() {
        log.info("Get all cook levels from database");

        List<CookLevelHibernateModel> response = getAllCookLevelsHibernateRepositoryAdapter.findAll();

        return response.stream().map(CookLevelHibernateModel::toDomain).toList();
    }
}
