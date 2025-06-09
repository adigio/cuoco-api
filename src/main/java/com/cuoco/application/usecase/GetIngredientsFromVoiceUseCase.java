package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetIngredientsFromVoiceCommand;
import com.cuoco.application.port.out.GetIngredientsFromVoiceRepository;
import com.cuoco.application.usecase.model.Ingredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class GetIngredientsFromVoiceUseCase implements GetIngredientsFromVoiceCommand {

    private static final Logger log = LoggerFactory.getLogger(GetIngredientsFromVoiceUseCase.class);

    private final GetIngredientsFromVoiceRepository getIngredientsFromVoiceRepository;

    public GetIngredientsFromVoiceUseCase(GetIngredientsFromVoiceRepository getIngredientsFromVoiceRepository) {
        this.getIngredientsFromVoiceRepository = getIngredientsFromVoiceRepository;
    }

    @Override
    public List<Ingredient> execute(Command command) {
        log.info("Executing get ingredients from voice use case - format: {}, language: {}",
                command.getFormat(), command.getLanguage());

        List<Ingredient> ingredients = getIngredientsFromVoiceRepository.processVoice(
                command.getAudioBase64(),
                command.getFormat(),
                command.getLanguage()
        );

        log.info("Successfully extracted {} ingredients from voice", ingredients.size());
        return ingredients;
    }


  @Override
    public CompletableFuture<List<Ingredient>> executeAsync(Command command) {
        log.info("Executing get ingredients from voice use case ASYNC - format: {}, language: {}",
                command.getFormat(), command.getLanguage());

        return getIngredientsFromVoiceRepository.processVoiceAsync(
                command.getAudioBase64(),
                command.getFormat(),
                command.getLanguage()
        ).thenApply(ingredients -> {
            log.info("Successfully extracted {} ingredients from voice ASYNC", ingredients.size());
            return ingredients;
        });
    }
}