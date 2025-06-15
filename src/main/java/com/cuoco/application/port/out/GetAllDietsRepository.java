package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.Diet;

import java.util.List;

public interface GetAllDietsRepository {
     List<Diet> execute();
}
