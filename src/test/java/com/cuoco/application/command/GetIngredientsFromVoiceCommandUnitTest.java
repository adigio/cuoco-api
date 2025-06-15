package com.cuoco.application.command;

import com.cuoco.application.port.in.GetIngredientsFromVoiceCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GetIngredientsFromVoiceCommandUnitTest {

    @Test
    void test1_command_shouldCreateCommandWithAllProperties() {
        // Given
        String audioBase64 = "base64EncodedAudioData";
        String format = "mp3";
        String language = "es-ES";

        // When
        GetIngredientsFromVoiceCommand.Command command =
                new GetIngredientsFromVoiceCommand.Command(audioBase64, format, language);

        // Then
        assertNotNull(command);
        assertEquals(audioBase64, command.getAudioBase64());
        assertEquals(format, command.getFormat());
        assertEquals(language, command.getLanguage());
    }

    @Test
    void test2_command_shouldHandleDifferentAudioFormats() {
        // Given
        String audioBase64 = "longBase64AudioString";
        String format = "wav";
        String language = "en-US";

        // When
        GetIngredientsFromVoiceCommand.Command command =
                new GetIngredientsFromVoiceCommand.Command(audioBase64, format, language);

        // Then
        assertEquals("wav", command.getFormat());
        assertEquals("en-US", command.getLanguage());
        assertEquals(audioBase64, command.getAudioBase64());
    }

    @Test
    void test3_command_shouldHandleEmptyValues() {
        // Given
        String audioBase64 = "";
        String format = "";
        String language = "";

        // When
        GetIngredientsFromVoiceCommand.Command command =
                new GetIngredientsFromVoiceCommand.Command(audioBase64, format, language);

        // Then
        assertNotNull(command);
        assertEquals("", command.getAudioBase64());
        assertEquals("", command.getFormat());
        assertEquals("", command.getLanguage());
    }

    @Test
    void test4_command_shouldHandleNullValues() {
        // Given
        String audioBase64 = null;
        String format = null;
        String language = null;

        // When
        GetIngredientsFromVoiceCommand.Command command =
                new GetIngredientsFromVoiceCommand.Command(audioBase64, format, language);

        // Then
        assertNotNull(command);
        assertNull(command.getAudioBase64());
        assertNull(command.getFormat());
        assertNull(command.getLanguage());
    }

    @Test
    void test5_command_shouldHandleComplexAudioBase64() {
        // Given
        String complexAudioBase64 = "UklGRiQNAABXQVZFZm10IBAAAAABAAEARKwAAIhYAQACABAAZGF0YQANAACIhYqFbF1fdJivrJBhNjBcEgAAOaV...";
        String format = "ogg";
        String language = "es-AR";

        // When
        GetIngredientsFromVoiceCommand.Command command =
                new GetIngredientsFromVoiceCommand.Command(complexAudioBase64, format, language);

        // Then
        assertEquals(complexAudioBase64, command.getAudioBase64());
        assertEquals("ogg", command.getFormat());
        assertEquals("es-AR", command.getLanguage());
        assertTrue(command.getAudioBase64().length() > 50);
    }
}