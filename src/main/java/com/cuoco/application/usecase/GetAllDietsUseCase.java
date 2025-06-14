package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetDietsQuery;
import com.cuoco.application.port.out.GetAllDietsRepository;
import com.cuoco.application.usecase.model.Diet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetAllDietsUseCase implements GetDietsQuery {

    static final Logger log = LoggerFactory.getLogger(GetAllDietsUseCase.class);

    private GetAllDietsRepository getAllDietsRepository;

    public GetAllDietsUseCase(GetAllDietsRepository getAllDietsRepository) {
        this.getAllDietsRepository = getAllDietsRepository;
    }

    @Override
    public List<Diet> execute() {
        log.info("Executing get all diets use case");
        return getAllDietsRepository.execute();
    }
}
