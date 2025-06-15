package com.cuoco.application.command;

import com.cuoco.application.port.in.GetIngredientsFromTextCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GetIngredientsFromTextCommand - 5 Unit Tests")
class GetIngredientsFromTextCommandUnitTest {

    @Test
    @DisplayName("Test 1: Command should be created with valid text")
    void test1_commandShouldBeCreatedWithValidText() {
        // Given
        String text = "tomate, cebolla, ajo";
        String source = "manual";

        // When
        GetIngredientsFromTextCommand.Command command =
                new GetIngredientsFromTextCommand.Command(text, source);

        // Then
        assertNotNull(command);
        assertEquals(text, command.getText());
        assertEquals(source, command.getSource());
    }

    @Test
    @DisplayName("Test 2: Command should handle empty text")
    void test2_commandShouldHandleEmptyText() {
        // Given
        String emptyText = "";
        String source = "manual";

        // When
        GetIngredientsFromTextCommand.Command command =
                new GetIngredientsFromTextCommand.Command(emptyText, source);

        // Then
        assertNotNull(command);
        assertEquals(emptyText, command.getText());
        assertEquals(source, command.getSource());
    }

    @Test
    @DisplayName("Test 3: Command should handle null source")
    void test3_commandShouldHandleNullSource() {
        // Given
        String text = "ingredientes";
        String nullSource = null;

        // When
        GetIngredientsFromTextCommand.Command command =
                new GetIngredientsFromTextCommand.Command(text, nullSource);

        // Then
        assertNotNull(command);
        assertEquals(text, command.getText());
        assertNull(command.getSource());
    }

    @Test
    @DisplayName("Test 4: Command should handle long text")
    void test4_commandShouldHandleLongText() {
        // Given
        String longText = "tomate, cebolla, ajo, pimiento rojo, aceite de oliva extra virgen, sal marina, pimienta negra recién molida";
        String source = "recipe";

        // When
        GetIngredientsFromTextCommand.Command command =
                new GetIngredientsFromTextCommand.Command(longText, source);

        // Then
        assertNotNull(command);
        assertEquals(longText, command.getText());
        assertEquals(source, command.getSource());
    }

    @Test
    @DisplayName("Test 5: Command should handle special characters")
    void test5_commandShouldHandleSpecialCharacters() {
        // Given
        String textWithSpecialChars = "ñoquis, café, açúcar, jalapeño";
        String source = "international";

        // When
        GetIngredientsFromTextCommand.Command command =
                new GetIngredientsFromTextCommand.Command(textWithSpecialChars, source);

        // Then
        assertNotNull(command);
        assertEquals(textWithSpecialChars, command.getText());
        assertEquals(source, command.getSource());
    }
}