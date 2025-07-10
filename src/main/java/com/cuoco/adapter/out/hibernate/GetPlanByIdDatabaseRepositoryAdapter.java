package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.PlanHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetPlanByIdHibernateRepositoryAdapter;
import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.out.GetPlanByIdRepository;
import com.cuoco.application.usecase.model.Plan;
import com.cuoco.shared.model.ErrorDescription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
public class GetPlanByIdDatabaseRepositoryAdapter implements GetPlanByIdRepository {

    private final GetPlanByIdHibernateRepositoryAdapter getPlanByIdHibernateRepositoryAdapter;

    public GetPlanByIdDatabaseRepositoryAdapter(GetPlanByIdHibernateRepositoryAdapter getPlanByIdHibernateRepositoryAdapter) {
        this.getPlanByIdHibernateRepositoryAdapter = getPlanByIdHibernateRepositoryAdapter;
    }

    @Override
    public Plan execute(Integer id) {

        Optional<PlanHibernateModel> plan = getPlanByIdHibernateRepositoryAdapter.findById(id);

        if (plan.isPresent()) {
            return plan.get().toDomain();
        } else {
            log.warn("Plan not found with id {}", id);
            throw new BadRequestException(ErrorDescription.PLAN_NOT_EXISTS.getValue());
        }
    }
}
