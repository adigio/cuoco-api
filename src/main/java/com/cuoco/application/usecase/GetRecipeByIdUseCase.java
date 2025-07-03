package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetRecipeByIdQuery;
import com.cuoco.application.port.out.GetRecipeByIdRepository;
import com.cuoco.application.usecase.model.Recipe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GetRecipeByIdUseCase implements GetRecipeByIdQuery {

    private final GetRecipeByIdRepository getRecipeByIdRepository;

    public GetRecipeByIdUseCase(GetRecipeByIdRepository getRecipeByIdRepository) {
        this.getRecipeByIdRepository = getRecipeByIdRepository;
    }

    @Override
    public Recipe execute(Long id) {
        log.info("Executing get recipe by id use case with ID: {}", id);
        return getRecipeByIdRepository.execute(id);
    }
}
