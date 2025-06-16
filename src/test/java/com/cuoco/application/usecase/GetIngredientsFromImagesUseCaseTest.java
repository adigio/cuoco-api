package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetIngredientsFromImagesCommand;
import com.cuoco.application.port.out.GetIngredientsFromImageRepository;
import com.cuoco.application.usecase.domainservice.FileDomainService;
import com.cuoco.application.usecase.model.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetIngredientsFromImagesUseCaseTest {

    private GetIngredientsFromImageRepository getIngredientsFromImageRepository;
    private FileDomainService fileDomainService;
    private MultipartFile imageFile1;
    private MultipartFile imageFile2;

    private GetIngredientsFromImagesUseCase useCase;

    @BeforeEach
    void setup() {
        getIngredientsFromImageRepository = mock(GetIngredientsFromImageRepository.class);
        fileDomainService = mock(FileDomainService.class);
        imageFile1 = mock(MultipartFile.class);
        imageFile2 = mock(MultipartFile.class);

        useCase = new GetIngredientsFromImagesUseCase(getIngredientsFromImageRepository, fileDomainService);
    }

    @Test
    void GIVEN_valid_images_WHEN_execute_THEN_return_ingredients_list() {
        List<MultipartFile> imageFiles = List.of(imageFile1, imageFile2);

        when(fileDomainService.getFileName(imageFile1)).thenReturn("file1.jpg");
        when(fileDomainService.convertToBase64(imageFile1)).thenReturn("base641");
        when(fileDomainService.getMimeType(imageFile1)).thenReturn("image/jpeg");

        when(fileDomainService.getFileName(imageFile2)).thenReturn("file2.png");
        when(fileDomainService.convertToBase64(imageFile2)).thenReturn("base642");
        when(fileDomainService.getMimeType(imageFile2)).thenReturn("image/png");

        List<Ingredient> ingredients = List.of(
                Ingredient.builder().name("Ingredient 1").build(),
                Ingredient.builder().name("Ingredient 2").build()
        );

        when(getIngredientsFromImageRepository.execute(anyList())).thenReturn(ingredients);

        GetIngredientsFromImagesCommand.Command command = GetIngredientsFromImagesCommand.Command.builder()
                .images(imageFiles)
                .build();

        List<Ingredient> result = useCase.execute(command);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Ingredient 1", result.get(0).getName());
        assertEquals("Ingredient 2", result.get(1).getName());
    }

    @Test
    void GIVEN_empty_image_list_WHEN_execute_THEN_return_empty_ingredient_list() {
        GetIngredientsFromImagesCommand.Command command = GetIngredientsFromImagesCommand.Command.builder()
                .images(List.of())
                .build();

        when(getIngredientsFromImageRepository.execute(anyList())).thenReturn(List.of());

        List<Ingredient> result = useCase.execute(command);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
