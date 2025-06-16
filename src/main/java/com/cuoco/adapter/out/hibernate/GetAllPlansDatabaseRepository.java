package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.PlanHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllPlansHibernateRepository;
import com.cuoco.application.port.out.GetAllPlansRepository;
import com.cuoco.application.usecase.model.Plan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class GetAllPlansDatabaseRepository implements GetAllPlansRepository {

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
