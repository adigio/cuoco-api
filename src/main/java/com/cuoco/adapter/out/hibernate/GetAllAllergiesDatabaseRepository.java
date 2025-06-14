package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.AllergyHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllAllergiesHibernateRepository;
import com.cuoco.application.port.out.GetAllAllergiesRepository;
import com.cuoco.application.usecase.model.Allergy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GetAllAllergiesDatabaseRepository implements GetAllAllergiesRepository {

    static final Logger log = LoggerFactory.getLogger(GetAllAllergiesDatabaseRepository.class);

    private final GetAllAllergiesHibernateRepository getAllAllergiesHibernateRepository;

    public GetAllAllergiesDatabaseRepository(GetAllAllergiesHibernateRepository getAllAllergiesHibernateRepository) {
        this.getAllAllergiesHibernateRepository = getAllAllergiesHibernateRepository;
    }

    @Override
    public List<Allergy> execute() {
        log.info("Get all allergies from database");

        List<AllergyHibernateModel> response = getAllAllergiesHibernateRepository.findAll();

        return response.stream().map(AllergyHibernateModel::toDomain).toList();
    }
}
