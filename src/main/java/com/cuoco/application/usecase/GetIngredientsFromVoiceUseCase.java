package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetIngredientsFromVoiceCommand;
import com.cuoco.application.port.out.GetIngredientsFromAudioRepository;
import com.cuoco.application.usecase.model.Ingredient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class GetIngredientsFromVoiceUseCase implements GetIngredientsFromVoiceCommand {

    private final GetIngredientsFromAudioRepository getIngredientsFromAudioRepository;

    public GetIngredientsFromVoiceUseCase(GetIngredientsFromAudioRepository getIngredientsFromAudioRepository) {
        this.getIngredientsFromAudioRepository = getIngredientsFromAudioRepository;
    }

    @Override
    public List<Ingredient> execute(Command command) {
        log.info("Executing get ingredients from voice use case - format: {}, language: {}",
                command.getFormat(), command.getLanguage());

        List<Ingredient> ingredients = getIngredientsFromAudioRepository.processVoice(
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

        return getIngredientsFromAudioRepository.processVoiceAsync(
                command.getAudioBase64(),
                command.getFormat(),
                command.getLanguage()
        ).thenApply(ingredients -> {
            log.info("Successfully extracted {} ingredients from voice ASYNC", ingredients.size());
            return ingredients;
        });
    }
}