package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetCookLevelsQuery;
import com.cuoco.application.port.out.GetAllCookLevelsRepository;
import com.cuoco.application.usecase.model.CookLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetAllCookLevelsUseCase implements GetCookLevelsQuery {

    static final Logger log = LoggerFactory.getLogger(GetAllCookLevelsUseCase.class);

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
