package com.cuoco.application.controller;

import com.cuoco.adapter.in.controller.UploadControllerAdapter;
import com.cuoco.adapter.in.controller.model.IngredientsResponse;
import com.cuoco.adapter.in.controller.model.IngredientsResponseMapper;
import com.cuoco.application.port.in.GetIngredientsFromFileCommand;
import com.cuoco.application.usecase.model.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UploadControllerAdapterUnitTest {

    @Mock
    private GetIngredientsFromFileCommand getIngredientsFromFileCommand;

    @Mock
    private IngredientsResponseMapper ingredientsResponseMapper;

    @Mock
    private MultipartFile multipartFile;

    private UploadControllerAdapter controller;

    @BeforeEach
    void setUp() {
        controller = new UploadControllerAdapter(getIngredientsFromFileCommand, ingredientsResponseMapper);
    }

    @Test
    void test1_shouldReturnIngredientsWhenValidFiles() {
        // Given
        List<MultipartFile> files = Arrays.asList(multipartFile);

        Map<String, List<Ingredient>> ingredientsByImage = new HashMap<>();
        ingredientsByImage.put("image1", Arrays.asList(
                new Ingredient("tomate", "imagen", true),
                new Ingredient("papa", "imagen", true)
        ));

        List<Ingredient> allIngredients = Arrays.asList(
                new Ingredient("tomate", "imagen", true),
                new Ingredient("papa", "imagen", true)
        );
        IngredientsResponse mockResponse = new IngredientsResponse(allIngredients);

        when(getIngredientsFromFileCommand.executeWithSeparation(any(GetIngredientsFromFileCommand.Command.class)))
                .thenReturn(ingredientsByImage);
        when(ingredientsResponseMapper.toImageSeparateResponse(ingredientsByImage))
                .thenReturn(mockResponse);

        // When
        ResponseEntity<?> response = controller.getIngredients(files);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof IngredientsResponse);
        verify(getIngredientsFromFileCommand).executeWithSeparation(any(GetIngredientsFromFileCommand.Command.class));
        verify(ingredientsResponseMapper).toImageSeparateResponse(ingredientsByImage);
    }

    @Test
    void test2_shouldReturnErrorWhenCommandThrowsException() {
        // Given
        List<MultipartFile> files = Arrays.asList(multipartFile);
        when(getIngredientsFromFileCommand.executeWithSeparation(any(GetIngredientsFromFileCommand.Command.class)))
                .thenThrow(new RuntimeException("Processing error"));

        // When
        ResponseEntity<?> response = controller.getIngredients(files);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error al procesar la imagen"));
        verify(getIngredientsFromFileCommand).executeWithSeparation(any(GetIngredientsFromFileCommand.Command.class));
        verifyNoInteractions(ingredientsResponseMapper);
    }

    @Test
    void test3_shouldHandleEmptyFilesList() {
        // Given
        List<MultipartFile> emptyFiles = Arrays.asList();
        Map<String, List<Ingredient>> emptyIngredientsByImage = new HashMap<>();
        IngredientsResponse mockResponse = new IngredientsResponse(Collections.emptyList());

        when(getIngredientsFromFileCommand.executeWithSeparation(any(GetIngredientsFromFileCommand.Command.class)))
                .thenReturn(emptyIngredientsByImage);
        when(ingredientsResponseMapper.toImageSeparateResponse(emptyIngredientsByImage))
                .thenReturn(mockResponse);

        // When
        ResponseEntity<?> response = controller.getIngredients(emptyFiles);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof IngredientsResponse);
        verify(getIngredientsFromFileCommand).executeWithSeparation(any(GetIngredientsFromFileCommand.Command.class));
        verify(ingredientsResponseMapper).toImageSeparateResponse(emptyIngredientsByImage);
    }

    @Test
    void test4_shouldCreateCorrectCommandFromFiles() {
        // Given
        List<MultipartFile> files = Arrays.asList(multipartFile);
        Map<String, List<Ingredient>> ingredientsByImage = new HashMap<>();
        ingredientsByImage.put("image1", Arrays.asList(new Ingredient("test", "imagen", true)));

        List<Ingredient> ingredients = Arrays.asList(new Ingredient("test", "imagen", true));
        IngredientsResponse mockResponse = new IngredientsResponse(ingredients);

        when(getIngredientsFromFileCommand.executeWithSeparation(any(GetIngredientsFromFileCommand.Command.class)))
                .thenReturn(ingredientsByImage);
        when(ingredientsResponseMapper.toImageSeparateResponse(ingredientsByImage))
                .thenReturn(mockResponse);

        // When
        controller.getIngredients(files);

        // Then
        verify(getIngredientsFromFileCommand).executeWithSeparation(argThat(command ->
                command.getFiles() != null && command.getFiles().size() == 1
        ));
        verify(ingredientsResponseMapper).toImageSeparateResponse(ingredientsByImage);
    }

    @Test
    void test5_shouldReturnIngredientsResponseWithCorrectFormat() {
        // Given
        List<MultipartFile> files = Arrays.asList(multipartFile);

        Map<String, List<Ingredient>> ingredientsByImage = new HashMap<>();
        ingredientsByImage.put("image1", Arrays.asList(
                new Ingredient("zanahoria", "imagen", true),
                new Ingredient("apio", "imagen", false)
        ));

        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("zanahoria", "imagen", true),
                new Ingredient("apio", "imagen", false)
        );
        IngredientsResponse mockResponse = new IngredientsResponse(ingredients);

        when(getIngredientsFromFileCommand.executeWithSeparation(any(GetIngredientsFromFileCommand.Command.class)))
                .thenReturn(ingredientsByImage);
        when(ingredientsResponseMapper.toImageSeparateResponse(ingredientsByImage))
                .thenReturn(mockResponse);

        // When
        ResponseEntity<?> response = controller.getIngredients(files);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        IngredientsResponse ingredientsResponse = (IngredientsResponse) response.getBody();
        assertNotNull(ingredientsResponse);
        assertEquals(2, ingredientsResponse.getIngredients().size());
        verify(getIngredientsFromFileCommand).executeWithSeparation(any(GetIngredientsFromFileCommand.Command.class));
        verify(ingredientsResponseMapper).toImageSeparateResponse(ingredientsByImage);
    }
}