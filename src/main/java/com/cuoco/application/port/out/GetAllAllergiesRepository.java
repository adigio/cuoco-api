package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.Allergy;

import java.util.List;

public interface GetAllAllergiesRepository {
    List<Allergy> execute();
}
