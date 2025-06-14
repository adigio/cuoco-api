package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.PlanHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetPlanByIdHibernateRepositoryAdapter;
import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.out.GetPlanByIdRepository;
import com.cuoco.application.usecase.model.Plan;
import com.cuoco.shared.model.ErrorDescription;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class GetPlanByIdDatabaseRepository implements GetPlanByIdRepository {

    private final GetPlanByIdHibernateRepositoryAdapter getPlanByIdHibernateRepositoryAdapter;

    public GetPlanByIdDatabaseRepository(GetPlanByIdHibernateRepositoryAdapter getPlanByIdHibernateRepositoryAdapter) {
        this.getPlanByIdHibernateRepositoryAdapter = getPlanByIdHibernateRepositoryAdapter;
    }

    @Override
    public Plan execute(Integer id) {

        Optional<PlanHibernateModel> plan = getPlanByIdHibernateRepositoryAdapter.findById(id);

        if (plan.isPresent()) {
            return plan.get().toDomain();
        } else {
            throw new BadRequestException(ErrorDescription.PLAN_NOT_EXISTS.getValue());
        }
    }
}
