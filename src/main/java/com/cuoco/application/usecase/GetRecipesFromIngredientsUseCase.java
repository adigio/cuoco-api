package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetRecipesFromIngredientsCommand;
import com.cuoco.application.port.out.GetRecipesFromIngredientsRepository;
import com.cuoco.application.usecase.model.Recipe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class GetRecipesFromIngredientsUseCase implements GetRecipesFromIngredientsCommand {

    private final GetRecipesFromIngredientsRepository getRecipesFromIngredientsRepository;

    public GetRecipesFromIngredientsUseCase(GetRecipesFromIngredientsRepository getRecipesFromIngredientsRepository) {
        this.getRecipesFromIngredientsRepository = getRecipesFromIngredientsRepository;
    }

    public List<Recipe> execute(Command command) {
        log.info("Executing get recipes from ingredients use case with command {}", command);
        return getRecipesFromIngredientsRepository.execute(command.getIngredients());
    }

}
