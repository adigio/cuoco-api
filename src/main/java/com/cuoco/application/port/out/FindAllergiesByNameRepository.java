package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.Allergies;

import java.util.List;

public interface FindAllergiesByNameRepository {
    List<Allergies> execute(List<String> allergiesNames);
}
