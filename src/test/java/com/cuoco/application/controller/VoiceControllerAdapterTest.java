package com.cuoco.application.controller;

import com.cuoco.adapter.in.controller.VoiceControllerAdapter;
import com.cuoco.adapter.in.controller.helper.AudioFileProcessor;
import com.cuoco.adapter.in.controller.model.IngredientsResponseMapper;
import com.cuoco.adapter.in.controller.model.IngredientsResponse;
import com.cuoco.application.port.in.GetIngredientsFromVoiceCommand;
import com.cuoco.application.usecase.model.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoiceControllerAdapterTest {

    @Mock
    private GetIngredientsFromVoiceCommand getIngredientsFromVoiceCommand;
    @Mock
    private IngredientsResponseMapper ingredientsResponseMapper;
    @Mock
    private AudioFileProcessor audioFileProcessor;

    private VoiceControllerAdapter controller;

    @BeforeEach
    void setUp() {
        controller = new VoiceControllerAdapter(
                getIngredientsFromVoiceCommand,
                ingredientsResponseMapper,
                audioFileProcessor
        );
    }

    @Test
    void test_analyzeVoice_shouldReturnIngredientsWhenValidAudio() throws Exception {
        // Given
        MockMultipartFile audioFile = new MockMultipartFile(
                "audio", "test.mp3", "audio/mp3", "fake audio content".getBytes()
        );

        List<Ingredient> ingredients = List.of(
                new Ingredient("tomate", "voz", false),
                new Ingredient("cebolla", "voz", false)
        );

        IngredientsResponse response = new IngredientsResponse(ingredients);

        // When
        when(audioFileProcessor.isValidAudioFile(audioFile)).thenReturn(true);
        when(audioFileProcessor.convertToBase64(audioFile)).thenReturn("base64Data");
        when(audioFileProcessor.getAudioFormat(audioFile)).thenReturn("mp3");
        when(getIngredientsFromVoiceCommand.execute(any())).thenReturn(ingredients);
        when(ingredientsResponseMapper.toResponse(ingredients)).thenReturn(response);

        ResponseEntity<?> result = controller.analyzeVoice(audioFile, "es-ES");

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(getIngredientsFromVoiceCommand).execute(any(GetIngredientsFromVoiceCommand.Command.class));
    }

    @Test
    void test_analyzeVoice_shouldReturnBadRequestWhenInvalidAudio() {
        // Given
        MockMultipartFile invalidFile = new MockMultipartFile(
                "audio", "test.txt", "text/plain", "not audio".getBytes()
        );

        // When
        when(audioFileProcessor.isValidAudioFile(invalidFile)).thenReturn(false);
        when(audioFileProcessor.getSupportedFormatsMessage()).thenReturn("Formato no válido");

        ResponseEntity<?> result = controller.analyzeVoice(invalidFile, "es-ES");

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Formato no válido", result.getBody());
        verifyNoInteractions(getIngredientsFromVoiceCommand);
    }
}