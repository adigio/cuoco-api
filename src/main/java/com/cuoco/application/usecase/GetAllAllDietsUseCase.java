package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetAllDietsQuery;
import com.cuoco.application.port.out.GetAllDietsRepository;
import com.cuoco.application.usecase.model.Diet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class GetAllAllDietsUseCase implements GetAllDietsQuery {

    private GetAllDietsRepository getAllDietsRepository;

    public GetAllAllDietsUseCase(GetAllDietsRepository getAllDietsRepository) {
        this.getAllDietsRepository = getAllDietsRepository;
    }

    @Override
    public List<Diet> execute() {
        log.info("Executing get all diets use case");
        return getAllDietsRepository.execute();
    }
}
