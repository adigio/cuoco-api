package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.CookLevel;

import java.util.List;

public interface GetAllCookLevelsRepository {
    List<CookLevel> execute();
}
