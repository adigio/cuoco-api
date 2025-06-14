package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetIngredientsFromFileCommand;
import com.cuoco.application.port.out.GetIngredientsFromRepository;
import com.cuoco.application.usecase.model.Ingredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GetIngredientsFromFileUseCase implements GetIngredientsFromFileCommand {

    static final Logger log = LoggerFactory.getLogger(GetIngredientsFromFileUseCase.class);

    private final GetIngredientsFromRepository getIngredientsFromRepository;

    public GetIngredientsFromFileUseCase(GetIngredientsFromRepository getIngredientsFromRepository) {
        this.getIngredientsFromRepository = getIngredientsFromRepository;
    }

    @Override
    public List<Ingredient> execute(GetIngredientsFromFileCommand.Command command) {
        log.info("Executing get ingredients from file use case with {} files", command.getFiles().size());

        List<Ingredient> ingredients = getIngredientsFromRepository.execute(command.getFiles());

        log.info("Successfully extracted {} ingredients from files", ingredients.size());

        return ingredients;
    }

    @Override
    public Map<String, List<Ingredient>> executeWithSeparation(GetIngredientsFromFileCommand.Command command) {
        log.info("Executing get ingredients from file use case with separation for {} files", command.getFiles().size());

        Map<String, List<Ingredient>> ingredientsByImage = getIngredientsFromRepository.executeWithSeparation(command.getFiles());

        log.info("Successfully extracted ingredients from {} files with separation", ingredientsByImage.size());

        return ingredientsByImage;
    }
}