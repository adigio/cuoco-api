package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetAllPreparationTimesQuery;
import com.cuoco.application.port.out.GetAllPreparationTimesRepository;
import com.cuoco.application.usecase.model.PreparationTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class GetAllPreparationTimesUseCase implements GetAllPreparationTimesQuery {

    private final GetAllPreparationTimesRepository getAllPreparationTimesRepository;

    public GetAllPreparationTimesUseCase(GetAllPreparationTimesRepository getAllPreparationTimesRepository) {
        this.getAllPreparationTimesRepository = getAllPreparationTimesRepository;
    }

    @Override
    public List<PreparationTime> execute() {
        log.info("Executing get all preparation times use case");
        return getAllPreparationTimesRepository.execute();
    }

}
