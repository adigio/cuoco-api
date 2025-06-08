package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.CookLevel;

public interface GetCookLevelByIdRepository {
    CookLevel execute(Integer id);
}
