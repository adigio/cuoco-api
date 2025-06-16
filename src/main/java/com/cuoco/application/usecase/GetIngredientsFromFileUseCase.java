package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetIngredientsFromFileCommand;
import com.cuoco.application.port.out.GetIngredientsFromImageRepository;
import com.cuoco.application.usecase.model.Ingredient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class GetIngredientsFromFileUseCase implements GetIngredientsFromFileCommand {

    private final GetIngredientsFromImageRepository getIngredientsFromImageRepository;

    public GetIngredientsFromFileUseCase(GetIngredientsFromImageRepository getIngredientsFromImageRepository) {
        this.getIngredientsFromImageRepository = getIngredientsFromImageRepository;
    }

    @Override
    public List<Ingredient> execute(GetIngredientsFromFileCommand.Command command) {
        log.info("Executing get ingredients from file use case with {} files", command.getFiles().size());

        List<Ingredient> ingredients = getIngredientsFromImageRepository.execute(command.getFiles());

        log.info("Successfully extracted {} ingredients from files", ingredients.size());

        return ingredients;
    }

    @Override
    public Map<String, List<Ingredient>> executeWithSeparation(GetIngredientsFromFileCommand.Command command) {
        log.info("Executing get ingredients from file use case with separation for {} files", command.getFiles().size());

        Map<String, List<Ingredient>> ingredientsByImage = getIngredientsFromImageRepository.executeWithSeparation(command.getFiles());

        log.info("Successfully extracted ingredients from {} files with separation", ingredientsByImage.size());

        return ingredientsByImage;
    }
}