package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.CookLevelHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllCookLevelsHibernateRepository;
import com.cuoco.application.port.out.GetAllCookLevelsRepository;
import com.cuoco.application.usecase.model.CookLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class GetAllCookLevelsDatabaseRepository implements GetAllCookLevelsRepository {

    private final GetAllCookLevelsHibernateRepository getAllCookLevelsHibernateRepository;

    public GetAllCookLevelsDatabaseRepository(GetAllCookLevelsHibernateRepository getAllCookLevelsHibernateRepository) {
        this.getAllCookLevelsHibernateRepository = getAllCookLevelsHibernateRepository;
    }

    @Override
    public List<CookLevel> execute() {
        log.info("Get all cook levels from database");

        List<CookLevelHibernateModel> response = getAllCookLevelsHibernateRepository.findAll();

        return response.stream().map(CookLevelHibernateModel::toDomain).toList();
    }
}
