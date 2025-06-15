package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.Allergy;

import java.util.List;

public interface GetAllAllergiesQuery {
    List<Allergy> execute();
}
