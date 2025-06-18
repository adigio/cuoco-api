package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.PlanHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllPlansHibernateRepositoryAdapter;
import com.cuoco.application.port.out.GetAllPlansRepository;
import com.cuoco.application.usecase.model.Plan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class GetAllPlansDatabaseRepositoryAdapter implements GetAllPlansRepository {

    private final GetAllPlansHibernateRepositoryAdapter getAllPlansHibernateRepositoryAdapter;

    public GetAllPlansDatabaseRepositoryAdapter(GetAllPlansHibernateRepositoryAdapter getAllPlansHibernateRepositoryAdapter) {
        this.getAllPlansHibernateRepositoryAdapter = getAllPlansHibernateRepositoryAdapter;
    }

    @Override
    public List<Plan> execute() {
        log.info("Get all plans from database");

        List<PlanHibernateModel> response = getAllPlansHibernateRepositoryAdapter.findAll();

        return response.stream().map(PlanHibernateModel::toDomain).toList();
    }
}
