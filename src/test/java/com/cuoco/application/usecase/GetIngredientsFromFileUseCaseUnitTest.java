package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetIngredientsFromFileCommand;
import com.cuoco.application.port.out.GetIngredientsFromImageRepository;
import com.cuoco.application.usecase.model.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetIngredientsFromFileUseCase - 5 Unit Tests")
class GetIngredientsFromFileUseCaseUnitTest {

    @Mock
    private GetIngredientsFromImageRepository repository;

    private GetIngredientsFromFileUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetIngredientsFromFileUseCase(repository);
    }

    @Test
    @DisplayName("Test 1: Should return ingredients when repository succeeds")
    void test1_shouldReturnIngredientsWhenRepositorySucceeds() {
        // Given
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "content".getBytes());
        List<MultipartFile> files = List.of(file);
        GetIngredientsFromFileCommand.Command command = new GetIngredientsFromFileCommand.Command(files);

        List<Ingredient> expectedIngredients = Arrays.asList(
                new Ingredient("tomate", "imagen", false),
                new Ingredient("cebolla", "imagen", false)
        );

        when(repository.execute(files)).thenReturn(expectedIngredients);

        // When
        List<Ingredient> result = useCase.execute(command);

        // Then
        assertEquals(2, result.size());
        assertEquals("tomate", result.get(0).getName());
        assertEquals("cebolla", result.get(1).getName());
        verify(repository).execute(files);
    }

    @Test
    @DisplayName("Test 2: Should handle empty files list")
    void test2_shouldHandleEmptyFilesList() {
        // Given
        List<MultipartFile> emptyFiles = List.of();
        GetIngredientsFromFileCommand.Command command = new GetIngredientsFromFileCommand.Command(emptyFiles);

        when(repository.execute(emptyFiles)).thenReturn(List.of());

        // When
        List<Ingredient> result = useCase.execute(command);

        // Then
        assertTrue(result.isEmpty());
        verify(repository).execute(emptyFiles);
    }

    @Test
    @DisplayName("Test 3: Should propagate repository exceptions")
    void test3_shouldPropagateRepositoryExceptions() {
        // Given
        MockMultipartFile file = new MockMultipartFile("file", "error.jpg", "image/jpeg", "content".getBytes());
        List<MultipartFile> files = List.of(file);
        GetIngredientsFromFileCommand.Command command = new GetIngredientsFromFileCommand.Command(files);

        when(repository.execute(files)).thenThrow(new RuntimeException("Repository error"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            useCase.execute(command);
        });

        assertEquals("Repository error", exception.getMessage());
        verify(repository).execute(files);
    }

    @Test
    @DisplayName("Test 4: Should pass correct files to repository")
    void test4_shouldPassCorrectFilesToRepository() {
        // Given
        MockMultipartFile file1 = new MockMultipartFile("file1", "test1.jpg", "image/jpeg", "content1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file2", "test2.jpg", "image/jpeg", "content2".getBytes());
        List<MultipartFile> files = List.of(file1, file2);
        GetIngredientsFromFileCommand.Command command = new GetIngredientsFromFileCommand.Command(files);

        when(repository.execute(any())).thenReturn(List.of());

        // When
        useCase.execute(command);

        // Then
        verify(repository).execute(argThat(fileList -> {
            assertEquals(2, fileList.size());
            assertEquals("test1.jpg", fileList.get(0).getOriginalFilename());
            assertEquals("test2.jpg", fileList.get(1).getOriginalFilename());
            return true;
        }));
    }

    @Test
    @DisplayName("Test 5: Should return correct ingredient structure")
    void test5_shouldReturnCorrectIngredientStructure() {
        // Given
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "content".getBytes());
        List<MultipartFile> files = List.of(file);
        GetIngredientsFromFileCommand.Command command = new GetIngredientsFromFileCommand.Command(files);

        List<Ingredient> repositoryResult = Arrays.asList(
                new Ingredient("zanahoria", "imagen", true),
                new Ingredient("apio", "imagen", false),
                new Ingredient("pimiento", "imagen", true)
        );

        when(repository.execute(files)).thenReturn(repositoryResult);

        // When
        List<Ingredient> result = useCase.execute(command);

        // Then
        assertEquals(3, result.size());

        // Verify first ingredient
        assertEquals("zanahoria", result.get(0).getName());
        assertEquals("imagen", result.get(0).getSource());
        assertTrue(result.get(0).isConfirmed());

        // Verify second ingredient
        assertEquals("apio", result.get(1).getName());
        assertEquals("imagen", result.get(1).getSource());
        assertFalse(result.get(1).isConfirmed());

        // Verify third ingredient
        assertEquals("pimiento", result.get(2).getName());
        assertEquals("imagen", result.get(2).getSource());
        assertTrue(result.get(2).isConfirmed());
    }
}