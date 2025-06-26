package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetAllMealTypesQuery;
import com.cuoco.application.port.out.GetAllMealTypesRepository;
import com.cuoco.application.usecase.model.MealType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class GetAllMealTypesUseCase implements GetAllMealTypesQuery {

    private final GetAllMealTypesRepository getAllMealTypesRepository;

    public GetAllMealTypesUseCase(GetAllMealTypesRepository getAllMealTypesRepository) {
        this.getAllMealTypesRepository = getAllMealTypesRepository;
    }

    @Override
    public List<MealType> execute() {
        log.info("Executing get all meal types use case");
        return getAllMealTypesRepository.execute();
    }
}
