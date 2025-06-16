package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetCookLevelsQuery;
import com.cuoco.application.port.out.GetAllCookLevelsRepository;
import com.cuoco.application.usecase.model.CookLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class GetAllCookLevelsUseCase implements GetCookLevelsQuery {

    private GetAllCookLevelsRepository getAllCookLevelsRepository;

    public GetAllCookLevelsUseCase(GetAllCookLevelsRepository getAllCookLevelsRepository) {
        this.getAllCookLevelsRepository = getAllCookLevelsRepository;
    }

    @Override
    public List<CookLevel> execute() {
        log.info("Executing get all cook levels use case");
        return getAllCookLevelsRepository.execute();
    }
}
