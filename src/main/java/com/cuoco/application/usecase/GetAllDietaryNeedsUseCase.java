package com.cuoco.application.usecase;

import com.cuoco.application.port.out.GetAllDietaryNeedsRepository;
import com.cuoco.application.port.in.GetAllDietaryNeedsQuery;
import com.cuoco.application.usecase.model.DietaryNeed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetAllDietaryNeedsUseCase implements GetAllDietaryNeedsQuery {

    static final Logger log = LoggerFactory.getLogger(GetAllDietaryNeedsUseCase.class);

    private GetAllDietaryNeedsRepository getAllDietaryNeedsRepository;

    public GetAllDietaryNeedsUseCase(GetAllDietaryNeedsRepository getAllDietaryNeedsRepository) {
        this.getAllDietaryNeedsRepository = getAllDietaryNeedsRepository;
    }

    @Override
    public List<DietaryNeed> execute() {
        log.info("Executing get all dietary needs use case");
        return getAllDietaryNeedsRepository.execute();
    }
}
