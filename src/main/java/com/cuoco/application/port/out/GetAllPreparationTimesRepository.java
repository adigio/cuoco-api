package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.PreparationTime;

import java.util.List;

public interface GetAllPreparationTimesRepository {
    List<PreparationTime> execute();
}
