package com.cuoco.application.usecase;

import com.cuoco.application.port.out.GetAllAllergiesRepository;
import com.cuoco.application.port.in.GetAllAllergiesQuery;
import com.cuoco.application.usecase.model.Allergy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetAllAllergiesUseCase implements GetAllAllergiesQuery {

    static final Logger log = LoggerFactory.getLogger(GetAllAllergiesUseCase.class);

    private GetAllAllergiesRepository getAllAllergiesRepository;

    public GetAllAllergiesUseCase(GetAllAllergiesRepository getAllAllergiesRepository) {
        this.getAllAllergiesRepository = getAllAllergiesRepository;
    }

    @Override
    public List<Allergy> execute() {
        log.info("Executing get all allergies use case");
        return getAllAllergiesRepository.execute();
    }
}
