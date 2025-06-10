package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.PlanHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllPlansHibernateRepository;
import com.cuoco.application.port.out.GetAllPlansRepository;
import com.cuoco.application.usecase.model.Plan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GetAllPlansDatabaseRepository implements GetAllPlansRepository {

    static final Logger log = LoggerFactory.getLogger(GetAllPlansDatabaseRepository.class);

    private final GetAllPlansHibernateRepository getAllPlansHibernateRepository;

    public GetAllPlansDatabaseRepository(GetAllPlansHibernateRepository getAllPlansHibernateRepository) {
        this.getAllPlansHibernateRepository = getAllPlansHibernateRepository;
    }

    @Override
    public List<Plan> execute() {
        log.info("Get all plans from database");

        List<PlanHibernateModel> response = getAllPlansHibernateRepository.findAll();

        return response.stream().map(PlanHibernateModel::toDomain).toList();
    }
}
