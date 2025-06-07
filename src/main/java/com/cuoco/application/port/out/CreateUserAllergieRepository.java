package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.Allergies;

import java.util.List;

public interface CreateUserAllergieRepository {
    void execute(Long userId, List<Allergies> allergies);
}
