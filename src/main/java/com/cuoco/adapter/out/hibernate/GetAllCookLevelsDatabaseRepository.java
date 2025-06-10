package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.CookLevelHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllCookLevelsHibernateRepository;
import com.cuoco.application.port.out.GetAllCookLevelsRepository;
import com.cuoco.application.usecase.model.CookLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GetAllCookLevelsDatabaseRepository implements GetAllCookLevelsRepository {

    static final Logger log = LoggerFactory.getLogger(GetAllCookLevelsDatabaseRepository.class);

    private final GetAllCookLevelsHibernateRepository getAllCookLevelsHibernateRepository;

    public GetAllCookLevelsDatabaseRepository(GetAllCookLevelsHibernateRepository getAllCookLevelsHibernateRepository) {
        this.getAllCookLevelsHibernateRepository = getAllCookLevelsHibernateRepository;
    }

    @Override
    public List<CookLevel> execute() {
        log.info("Get all cook levels from database");

        List<CookLevelHibernateModel> cookLevelsResponse = getAllCookLevelsHibernateRepository.findAll();

        return cookLevelsResponse.stream().map(CookLevelHibernateModel::toDomain).toList();
    }
}
