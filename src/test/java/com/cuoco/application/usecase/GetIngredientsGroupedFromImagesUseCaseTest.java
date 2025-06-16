package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetIngredientsGroupedFromImagesCommand;
import com.cuoco.application.port.out.GetIngredientsGroupedFromImagesRepository;
import com.cuoco.application.usecase.domainservice.FileDomainService;
import com.cuoco.application.usecase.model.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetIngredientsGroupedFromImagesUseCaseTest {

    private GetIngredientsGroupedFromImagesRepository getIngredientsGroupedFromImagesRepository;
    private FileDomainService fileDomainService;
    private MultipartFile imageFile1;
    private MultipartFile imageFile2;

    private GetIngredientsGroupedFromImagesUseCase useCase;

    @BeforeEach
    void setup() {
        getIngredientsGroupedFromImagesRepository = mock(GetIngredientsGroupedFromImagesRepository.class);
        fileDomainService = mock(FileDomainService.class);
        imageFile1 = mock(MultipartFile.class);
        imageFile2 = mock(MultipartFile.class);

        useCase = new GetIngredientsGroupedFromImagesUseCase(getIngredientsGroupedFromImagesRepository, fileDomainService);
    }

    @Test
    void GIVEN_valid_images_WHEN_execute_THEN_return_ingredients_grouped_by_filename() {
        List<MultipartFile> imageFiles = List.of(imageFile1, imageFile2);

        when(fileDomainService.getFileName(imageFile1)).thenReturn("image1.jpg");
        when(fileDomainService.convertToBase64(imageFile1)).thenReturn("base641");
        when(fileDomainService.getMimeType(imageFile1)).thenReturn("image/jpeg");

        when(fileDomainService.getFileName(imageFile2)).thenReturn("image2.jpg");
        when(fileDomainService.convertToBase64(imageFile2)).thenReturn("base642");
        when(fileDomainService.getMimeType(imageFile2)).thenReturn("image/jpeg");

        Map<String, List<Ingredient>> expectedMap = Map.of(
                "image1.jpg", List.of(Ingredient.builder().name("Tomato").build()),
                "image2.jpg", List.of(Ingredient.builder().name("Lettuce").build())
        );

        when(getIngredientsGroupedFromImagesRepository.execute(anyList())).thenReturn(expectedMap);

        GetIngredientsGroupedFromImagesCommand.Command command = GetIngredientsGroupedFromImagesCommand.Command.builder()
                .images(imageFiles)
                .build();

        Map<String, List<Ingredient>> result = useCase.execute(command);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsKey("image1.jpg"));
        assertEquals("Tomato", result.get("image1.jpg").get(0).getName());
    }

    @Test
    void GIVEN_empty_image_list_WHEN_execute_THEN_return_empty_map() {
        GetIngredientsGroupedFromImagesCommand.Command command = GetIngredientsGroupedFromImagesCommand.Command.builder()
                .images(List.of())
                .build();

        when(getIngredientsGroupedFromImagesRepository.execute(anyList())).thenReturn(Map.of());

        Map<String, List<Ingredient>> result = useCase.execute(command);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
