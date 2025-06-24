package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.PreparationTime;

import java.util.List;

public interface GetAllPreparationTimesQuery {
    List<PreparationTime> execute();
}
