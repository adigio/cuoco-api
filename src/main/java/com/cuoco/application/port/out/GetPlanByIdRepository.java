package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.Plan;

public interface GetPlanByIdRepository {
    Plan execute(Integer id);
}
