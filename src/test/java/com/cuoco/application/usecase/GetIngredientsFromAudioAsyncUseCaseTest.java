package com.cuoco.application.usecase;

import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.in.GetIngredientsFromAudioAsyncCommand;
import com.cuoco.application.port.out.GetIngredientsFromAudioAsyncRepository;
import com.cuoco.application.usecase.domainservice.FileDomainService;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.shared.model.ErrorDescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetIngredientsFromAudioAsyncUseCaseTest {

    private GetIngredientsFromAudioAsyncRepository getIngredientsFromAudioAsyncRepository;
    private FileDomainService fileDomainService;
    private MultipartFile multipartFile;

    private GetIngredientsFromAudioAsyncUseCase useCase;

    @BeforeEach
    void setup() {
        getIngredientsFromAudioAsyncRepository = mock(GetIngredientsFromAudioAsyncRepository.class);
        fileDomainService = mock(FileDomainService.class);
        multipartFile = mock(MultipartFile.class);

        useCase = new GetIngredientsFromAudioAsyncUseCase(getIngredientsFromAudioAsyncRepository, fileDomainService);
    }

    @Test
    void GIVEN_valid_audio_WHEN_execute_THEN_return_ingredients_list() throws Exception {
        String base64 = "base64string";
        String format = "mp3";

        List<Ingredient> ingredients = List.of(
                Ingredient.builder().name("Ingredient 1").build(),
                Ingredient.builder().name("Ingredient 2").build()
        );

        GetIngredientsFromAudioAsyncCommand.Command command = GetIngredientsFromAudioAsyncCommand.Command.builder()
                .audioFile(multipartFile)
                .language("en")
                .build();

        when(multipartFile.isEmpty()).thenReturn(false);
        when(fileDomainService.isValidAudioFile(multipartFile)).thenReturn(false);
        when(fileDomainService.convertToBase64(multipartFile)).thenReturn(base64);
        when(fileDomainService.getAudioFormat(multipartFile)).thenReturn(format);
        when(getIngredientsFromAudioAsyncRepository.execute(base64, format, "en"))
                .thenReturn(CompletableFuture.completedFuture(ingredients));

        CompletableFuture<List<Ingredient>> futureResult = useCase.execute(command);

        List<Ingredient> result = futureResult.get();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Ingredient 1", result.get(0).getName());
    }

    @Test
    void GIVEN_null_audio_file_WHEN_execute_THEN_throw_bad_request() {
        GetIngredientsFromAudioAsyncCommand.Command command = GetIngredientsFromAudioAsyncCommand.Command.builder()
                .audioFile(null)
                .build();

        BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(command));
        assertEquals(ErrorDescription.AUDIO_FILE_IS_REQUIRED.getValue(), ex.getDescription());
    }

    @Test
    void GIVEN_empty_audio_file_WHEN_execute_THEN_throw_bad_request() {
        when(multipartFile.isEmpty()).thenReturn(true);

        GetIngredientsFromAudioAsyncCommand.Command command = GetIngredientsFromAudioAsyncCommand.Command.builder()
                .audioFile(multipartFile)
                .build();

        BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(command));
        assertEquals(ErrorDescription.AUDIO_FILE_IS_REQUIRED.getValue(), ex.getDescription());
    }

    @Test
    void GIVEN_invalid_audio_format_WHEN_execute_THEN_throw_bad_request() {
        when(multipartFile.isEmpty()).thenReturn(false);
        when(fileDomainService.isValidAudioFile(multipartFile)).thenReturn(true);

        GetIngredientsFromAudioAsyncCommand.Command command = GetIngredientsFromAudioAsyncCommand.Command.builder()
                .audioFile(multipartFile)
                .build();

        BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(command));
        assertEquals(ErrorDescription.INVALID_AUDIO_FILE_EXTENSION.getValue(), ex.getDescription());
    }
}
