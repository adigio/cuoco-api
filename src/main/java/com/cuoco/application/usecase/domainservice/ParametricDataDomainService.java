package com.cuoco.application.usecase.domainservice;

import com.cuoco.application.port.out.GetAllAllergiesRepository;
import com.cuoco.application.port.out.GetAllCookLevelsRepository;
import com.cuoco.application.port.out.GetAllDietaryNeedsRepository;
import com.cuoco.application.port.out.GetAllDietsRepository;
import com.cuoco.application.port.out.GetAllMealTypesRepository;
import com.cuoco.application.port.out.GetAllPreparationTimesRepository;
import com.cuoco.application.port.out.GetAllUnitsRepository;
import com.cuoco.application.usecase.model.ParametricData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParametricDataDomainService {

    private final GetAllUnitsRepository getAllUnitsRepository;
    private final GetAllPreparationTimesRepository getAllPreparationTimesRepository;
    private final GetAllCookLevelsRepository getAllCookLevelsRepository;
    private final GetAllDietsRepository getAllDietsRepository;
    private final GetAllMealTypesRepository getAllMealTypesRepository;
    private final GetAllAllergiesRepository getAllAllergiesRepository;
    private final GetAllDietaryNeedsRepository getAllDietaryNeedsRepository;

    public ParametricData getAll() {
        return ParametricData.builder()
                .units(getAllUnitsRepository.execute())
                .preparationTimes(getAllPreparationTimesRepository.execute())
                .cookLevels(getAllCookLevelsRepository.execute())
                .diets(getAllDietsRepository.execute())
                .mealTypes(getAllMealTypesRepository.execute())
                .allergies(getAllAllergiesRepository.execute())
                .dietaryNeeds(getAllDietaryNeedsRepository.execute())
                .build();
    }
}
