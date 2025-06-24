package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.Unit;

import java.util.List;

public interface GetAllUnitsQuery {
    List<Unit> execute();
}
