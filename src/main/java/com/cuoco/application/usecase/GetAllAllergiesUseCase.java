package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetAllAllergiesQuery;
import com.cuoco.application.port.out.GetAllAllergiesRepository;
import com.cuoco.application.usecase.model.Allergy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class GetAllAllergiesUseCase implements GetAllAllergiesQuery {

    private final GetAllAllergiesRepository getAllAllergiesRepository;

    public GetAllAllergiesUseCase(GetAllAllergiesRepository getAllAllergiesRepository) {
        this.getAllAllergiesRepository = getAllAllergiesRepository;
    }

    @Override
    public List<Allergy> execute() {
        log.info("Executing get all allergies use case");
        return getAllAllergiesRepository.execute();
    }
}
