package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetAllDietaryNeedsQuery;
import com.cuoco.application.port.out.GetAllDietaryNeedsRepository;
import com.cuoco.application.usecase.model.DietaryNeed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class GetAllDietaryNeedsUseCase implements GetAllDietaryNeedsQuery {

    private final GetAllDietaryNeedsRepository getAllDietaryNeedsRepository;

    public GetAllDietaryNeedsUseCase(GetAllDietaryNeedsRepository getAllDietaryNeedsRepository) {
        this.getAllDietaryNeedsRepository = getAllDietaryNeedsRepository;
    }

    @Override
    public List<DietaryNeed> execute() {
        log.info("Executing get all dietary needs use case");
        return getAllDietaryNeedsRepository.execute();
    }
}
