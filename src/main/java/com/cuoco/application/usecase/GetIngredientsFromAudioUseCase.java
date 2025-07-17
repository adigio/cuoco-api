package com.cuoco.application.usecase;

import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.in.GetIngredientsFromAudioCommand;
import com.cuoco.application.port.out.GetIngredientsFromAudioRepository;
import com.cuoco.application.usecase.domainservice.FileDomainService;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.shared.model.ErrorDescription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class GetIngredientsFromAudioUseCase implements GetIngredientsFromAudioCommand {

    private final GetIngredientsFromAudioRepository getIngredientsFromAudioRepository;
    private final FileDomainService fileDomainService;

    public GetIngredientsFromAudioUseCase(
            GetIngredientsFromAudioRepository getIngredientsFromAudioRepository,
            FileDomainService fileDomainService
    ) {
        this.getIngredientsFromAudioRepository = getIngredientsFromAudioRepository;
        this.fileDomainService = fileDomainService;
    }

    @Override
    public List<Ingredient> execute(Command command) {
        log.info("Executing get ingredients from voice audio file use case");

        if (command.getAudioFile() == null || command.getAudioFile().isEmpty()) {
            throw new BadRequestException(ErrorDescription.AUDIO_FILE_IS_REQUIRED.getValue());
        }

        if (fileDomainService.isValidAudioFile(command.getAudioFile())) {
            throw new BadRequestException(ErrorDescription.INVALID_AUDIO_FILE_EXTENSION.getValue());
        }

        String audioBase64 = fileDomainService.convertToBase64(command.getAudioFile());
        String format = fileDomainService.getAudioFormat(command.getAudioFile());

        List<Ingredient> ingredients = getIngredientsFromAudioRepository.execute(
                audioBase64,
                format,
                command.getLanguage()
        );

        log.info("Successfully extracted {} ingredients from voice", ingredients.size());

        return ingredients;
    }
}