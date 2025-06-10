package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.DietaryNeedHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllDietaryNeedsHibernateRepository;
import com.cuoco.application.port.out.GetAllDietaryNeedsRepository;
import com.cuoco.application.usecase.model.DietaryNeed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GetAllDietaryNeedsDatabaseRepository implements GetAllDietaryNeedsRepository {

    static final Logger log = LoggerFactory.getLogger(GetAllDietaryNeedsDatabaseRepository.class);

    private final GetAllDietaryNeedsHibernateRepository getAllDietaryNeedsHibernateRepository;

    public GetAllDietaryNeedsDatabaseRepository(GetAllDietaryNeedsHibernateRepository getAllDietaryNeedsHibernateRepository) {
        this.getAllDietaryNeedsHibernateRepository = getAllDietaryNeedsHibernateRepository;
    }

    @Override
    public List<DietaryNeed> execute() {
        log.info("Get all dietary needs from database");

        List<DietaryNeedHibernateModel> response = getAllDietaryNeedsHibernateRepository.findAll();

        return response.stream().map(DietaryNeedHibernateModel::toDomain).toList();
    }
}
