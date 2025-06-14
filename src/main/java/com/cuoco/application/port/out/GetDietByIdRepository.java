package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.Diet;

public interface GetDietByIdRepository {
    Diet execute(Integer id);
}
