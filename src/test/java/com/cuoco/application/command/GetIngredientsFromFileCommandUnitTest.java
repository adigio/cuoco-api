package com.cuoco.application.command;

import com.cuoco.application.port.in.GetIngredientsFromFileCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetIngredientsFromFileCommand - 5 Unit Tests")
class GetIngredientsFromFileCommandUnitTest {

    @Test
    @DisplayName("Test 1: Command should be created with valid files")
    void test1_commandShouldBeCreatedWithValidFiles() {
        // Given
        MockMultipartFile file1 = new MockMultipartFile("file1", "test1.jpg", "image/jpeg", "content1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file2", "test2.jpg", "image/jpeg", "content2".getBytes());
        List<MultipartFile> files = Arrays.asList(file1, file2);

        // When
        GetIngredientsFromFileCommand.Command command = new GetIngredientsFromFileCommand.Command(files);

        // Then
        assertNotNull(command);
        assertEquals(files, command.getFiles());
        assertEquals(2, command.getFiles().size());
        assertEquals("test1.jpg", command.getFiles().get(0).getOriginalFilename());
        assertEquals("test2.jpg", command.getFiles().get(1).getOriginalFilename());
    }

    @Test
    @DisplayName("Test 2: Command should handle empty files list")
    void test2_commandShouldHandleEmptyFilesList() {
        // Given
        List<MultipartFile> emptyFiles = Collections.emptyList();

        // When
        GetIngredientsFromFileCommand.Command command = new GetIngredientsFromFileCommand.Command(emptyFiles);

        // Then
        assertNotNull(command);
        assertEquals(emptyFiles, command.getFiles());
        assertTrue(command.getFiles().isEmpty());
    }

    @Test
    @DisplayName("Test 3: Command should handle single file")
    void test3_commandShouldHandleSingleFile() {
        // Given
        MockMultipartFile singleFile = new MockMultipartFile("file", "single.png", "image/png", "single content".getBytes());
        List<MultipartFile> files = Arrays.asList(singleFile);

        // When
        GetIngredientsFromFileCommand.Command command = new GetIngredientsFromFileCommand.Command(files);

        // Then
        assertNotNull(command);
        assertEquals(files, command.getFiles());
        assertEquals(1, command.getFiles().size());
        assertEquals("single.png", command.getFiles().get(0).getOriginalFilename());
        assertEquals("image/png", command.getFiles().get(0).getContentType());
    }

    @Test
    @DisplayName("Test 4: Command should handle different file types")
    void test4_commandShouldHandleDifferentFileTypes() {
        // Given
        MockMultipartFile jpegFile = new MockMultipartFile("file1", "image.jpg", "image/jpeg", "jpeg content".getBytes());
        MockMultipartFile pngFile = new MockMultipartFile("file2", "image.png", "image/png", "png content".getBytes());
        MockMultipartFile webpFile = new MockMultipartFile("file3", "image.webp", "image/webp", "webp content".getBytes());
        List<MultipartFile> files = Arrays.asList(jpegFile, pngFile, webpFile);

        // When
        GetIngredientsFromFileCommand.Command command = new GetIngredientsFromFileCommand.Command(files);

        // Then
        assertNotNull(command);
        assertEquals(3, command.getFiles().size());
        assertEquals("image/jpeg", command.getFiles().get(0).getContentType());
        assertEquals("image/png", command.getFiles().get(1).getContentType());
        assertEquals("image/webp", command.getFiles().get(2).getContentType());
    }

    @Test
    @DisplayName("Test 5: Command should handle null files list")
    void test5_commandShouldHandleNullFilesList() {
        // Given
        List<MultipartFile> nullFiles = null;

        // When
        GetIngredientsFromFileCommand.Command command = new GetIngredientsFromFileCommand.Command(nullFiles);

        // Then
        assertNotNull(command);
        assertNull(command.getFiles());
    }

    @Test
    @DisplayName("Test 6: Command should have proper toString method")
    void test6_commandShouldHaveProperToStringMethod() {
        // Given
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "content".getBytes());
        List<MultipartFile> files = Arrays.asList(file);

        // When
        GetIngredientsFromFileCommand.Command command = new GetIngredientsFromFileCommand.Command(files);
        String result = command.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("Command"));
        assertTrue(result.contains("files"));
    }
}