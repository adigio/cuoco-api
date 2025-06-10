package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.DietHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllDietsHibernateRepository;
import com.cuoco.application.port.out.GetAllDietsRepository;
import com.cuoco.application.usecase.model.Diet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GetAllDietsDatabaseRepository implements GetAllDietsRepository {

    static final Logger log = LoggerFactory.getLogger(GetAllDietsDatabaseRepository.class);

    private final GetAllDietsHibernateRepository getAllDietsHibernateRepository;

    public GetAllDietsDatabaseRepository(GetAllDietsHibernateRepository getAllDietsHibernateRepository) {
        this.getAllDietsHibernateRepository = getAllDietsHibernateRepository;
    }

    @Override
    public List<Diet> execute() {
        log.info("Get all diets from database");

        List<DietHibernateModel> response = getAllDietsHibernateRepository.findAll();

        return response.stream().map(DietHibernateModel::toDomain).toList();
    }
}
