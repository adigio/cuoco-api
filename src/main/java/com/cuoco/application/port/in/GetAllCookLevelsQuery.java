package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.CookLevel;

import java.util.List;

public interface GetAllCookLevelsQuery {
    List<CookLevel> execute();
}
