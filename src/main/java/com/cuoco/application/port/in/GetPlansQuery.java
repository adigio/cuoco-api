package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.Plan;

import java.util.List;

public interface GetPlansQuery {
    List<Plan> execute();
}
