package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetPlansQuery;
import com.cuoco.application.port.out.GetAllPlansRepository;
import com.cuoco.application.usecase.model.Plan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetAllPlansUseCase implements GetPlansQuery {

    static final Logger log = LoggerFactory.getLogger(GetAllPlansUseCase.class);

    private GetAllPlansRepository getAllPlansRepository;

    public GetAllPlansUseCase(GetAllPlansRepository getAllPlansRepository) {
        this.getAllPlansRepository = getAllPlansRepository;
    }

    @Override
    public List<Plan> execute() {
        log.info("Executing get all plans use case");
        return getAllPlansRepository.execute();
    }
}
