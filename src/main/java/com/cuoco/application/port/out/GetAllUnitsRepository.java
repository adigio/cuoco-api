package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.Unit;

import java.util.List;

public interface GetAllUnitsRepository {
    List<Unit> execute();
}
