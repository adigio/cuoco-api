package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetMealPrepByIdQuery;
import com.cuoco.application.port.out.GetMealPrepByIdRepository;
import com.cuoco.application.usecase.model.MealPrep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GetMealPrepByIdUseCase implements GetMealPrepByIdQuery {

    private final GetMealPrepByIdRepository getMealPrepByIdRepository;

    public GetMealPrepByIdUseCase(GetMealPrepByIdRepository getMealPrepByIdRepository) {
        this.getMealPrepByIdRepository = getMealPrepByIdRepository;
    }

    @Override
    public MealPrep execute(Long id) {
        log.info("Executing get meal prep by id use case with ID: {}", id);
        return getMealPrepByIdRepository.execute(id);
    }
}
