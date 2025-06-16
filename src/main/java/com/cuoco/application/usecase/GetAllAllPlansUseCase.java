package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetAllPlansQuery;
import com.cuoco.application.port.out.GetAllPlansRepository;
import com.cuoco.application.usecase.model.Plan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class GetAllAllPlansUseCase implements GetAllPlansQuery {

    private GetAllPlansRepository getAllPlansRepository;

    public GetAllAllPlansUseCase(GetAllPlansRepository getAllPlansRepository) {
        this.getAllPlansRepository = getAllPlansRepository;
    }

    @Override
    public List<Plan> execute() {
        log.info("Executing get all plans use case");
        return getAllPlansRepository.execute();
    }
}
