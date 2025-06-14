package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetIngredientsFromTextCommand;
import com.cuoco.application.port.out.GetIngredientsFromTextRepository;
import com.cuoco.application.usecase.model.Ingredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetIngredientsFromTextUseCase implements GetIngredientsFromTextCommand {

    static final Logger log = LoggerFactory.getLogger(GetIngredientsFromTextUseCase.class);

    private final GetIngredientsFromTextRepository getIngredientsFromTextRepository;

    public GetIngredientsFromTextUseCase(GetIngredientsFromTextRepository getIngredientsFromTextRepository) {
        this.getIngredientsFromTextRepository = getIngredientsFromTextRepository;
    }

    @Override
    public List<Ingredient> execute(GetIngredientsFromTextCommand.Command command) {
        log.info("Executing get ingredients from text use case with text: {}", command.getText());

        List<Ingredient> ingredients = getIngredientsFromTextRepository.execute(command.getText());

        log.info("Successfully extracted {} ingredients from text", ingredients.size());

        return ingredients;
    }
}