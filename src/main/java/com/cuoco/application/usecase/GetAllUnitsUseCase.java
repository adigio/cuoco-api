package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetAllUnitsQuery;
import com.cuoco.application.port.out.GetAllUnitsRepository;
import com.cuoco.application.usecase.model.Unit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class GetAllUnitsUseCase implements GetAllUnitsQuery {

    private GetAllUnitsRepository getAllUnitsRepository;

    public GetAllUnitsUseCase(GetAllUnitsRepository getAllUnitsRepository) {
        this.getAllUnitsRepository = getAllUnitsRepository;
    }

    @Override
    public List<Unit> execute() {
        log.info("Executing get all units use case");
        return getAllUnitsRepository.execute();
    }

}
