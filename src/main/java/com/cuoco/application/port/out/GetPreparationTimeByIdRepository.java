package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.PreparationTime;

public interface GetPreparationTimeByIdRepository {
    PreparationTime execute(Integer id);
}
