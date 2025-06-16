package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.Diet;

import java.util.List;

public interface GetAllDietsQuery {
    List<Diet> execute();
}
