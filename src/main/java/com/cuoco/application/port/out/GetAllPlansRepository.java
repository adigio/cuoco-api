package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.Plan;

import java.util.List;

public interface GetAllPlansRepository {
    List<Plan> execute();
}
