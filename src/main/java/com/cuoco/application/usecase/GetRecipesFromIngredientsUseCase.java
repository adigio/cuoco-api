package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetRecipesFromIngredientsCommand;
import com.cuoco.application.port.out.GetRecipesFromIngredientsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GetRecipesFromIngredientsUseCase implements GetRecipesFromIngredientsCommand {

    static final Logger log = LoggerFactory.getLogger(GetRecipesFromIngredientsUseCase.class);

    private final GetRecipesFromIngredientsRepository getRecipesFromIngredientsRepository;

    public GetRecipesFromIngredientsUseCase(GetRecipesFromIngredientsRepository getRecipesFromIngredientsRepository) {
        this.getRecipesFromIngredientsRepository = getRecipesFromIngredientsRepository;
    }

    public String execute(Command command) {
        log.info("Executing get recipes from ingredients use case with command {}", command);

        String recipes = getRecipesFromIngredientsRepository.execute(command.getIngredients());

        return recipes;
    }

}
