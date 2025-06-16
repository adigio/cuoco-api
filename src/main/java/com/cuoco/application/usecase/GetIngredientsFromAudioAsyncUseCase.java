package com.cuoco.application.usecase;

import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.in.GetIngredientsFromAudioAsyncCommand;
import com.cuoco.application.port.out.GetIngredientsFromAudioAsyncRepository;
import com.cuoco.application.usecase.domainservice.FileDomainService;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.shared.model.ErrorDescription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class GetIngredientsFromAudioAsyncUseCase implements GetIngredientsFromAudioAsyncCommand {

    private final GetIngredientsFromAudioAsyncRepository getIngredientsFromAudioAsyncRepository;
    private final FileDomainService fileDomainService;

    public GetIngredientsFromAudioAsyncUseCase(
            GetIngredientsFromAudioAsyncRepository getIngredientsFromAudioAsyncRepository,
            FileDomainService fileDomainService
    ) {
        this.getIngredientsFromAudioAsyncRepository = getIngredientsFromAudioAsyncRepository;
        this.fileDomainService = fileDomainService;
    }

    @Override
    public CompletableFuture<List<Ingredient>> execute(GetIngredientsFromAudioAsyncCommand.Command command) {
        log.info("Executing asynchronously get ingredients from audio use case");

        if (command.getAudioFile() == null || command.getAudioFile().isEmpty()) {
            throw new BadRequestException(ErrorDescription.AUDIO_FILE_IS_REQUIRED.getValue());
        }

        if (fileDomainService.isValidAudioFile(command.getAudioFile())) {
            throw new BadRequestException(ErrorDescription.INVALID_AUDIO_FILE_EXTENSION.getValue());
        }

        String audioBase64 = fileDomainService.convertToBase64(command.getAudioFile());
        String format = fileDomainService.getAudioFormat(command.getAudioFile());

        return getIngredientsFromAudioAsyncRepository.execute(
                audioBase64,
                format,
                command.getLanguage()
        ).thenApply(ingredients -> {
            log.info("Successfully extracted {} ingredients from voice ASYNC", ingredients.size());
            return ingredients;
        });
    }
}
