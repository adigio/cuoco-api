package com.cuoco.adapter.out.rest.gemini;

import com.cuoco.adapter.exception.UnprocessableException;
import com.cuoco.application.port.out.GetIngredientsFromAudioAsyncRepository;
import com.cuoco.application.port.out.GetIngredientsFromAudioRepository;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.shared.model.ErrorDescription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class GetIngredientsFromAudioAsyncGeminiRestRepositoryAdapter implements GetIngredientsFromAudioAsyncRepository {

    private final GetIngredientsFromAudioRepository getIngredientsFromAudioRepository;

    public GetIngredientsFromAudioAsyncGeminiRestRepositoryAdapter(GetIngredientsFromAudioRepository getIngredientsFromAudioRepository) {
        this.getIngredientsFromAudioRepository = getIngredientsFromAudioRepository;
    }

    @Async
    @Override
    public CompletableFuture<List<Ingredient>> execute(String audioBase64, String format, String language) {
        log.info("Executing asynchronous voice processing in Gemini with format {} and language {}", format, language);

        return CompletableFuture.supplyAsync(() -> {
            try {
                return getIngredientsFromAudioRepository.execute(audioBase64, format, language);
            } catch (Exception e) {
                log.error("Async error processing voice: {}", e.getMessage(), e);
                throw new UnprocessableException(ErrorDescription.AUDIO_FILE_PROCESSING_ERROR.getValue());
            }
        });
    }

}
