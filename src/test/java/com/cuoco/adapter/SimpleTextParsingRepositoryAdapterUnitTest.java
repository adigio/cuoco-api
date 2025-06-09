package com.cuoco.adapter;

import com.cuoco.adapter.out.hibernate.SimpleTextParsingRepositoryAdapter;
import com.cuoco.application.usecase.model.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SimpleTextParsingRepositoryAdapter - TDD")
class SimpleTextParsingRepositoryAdapterUnitTest {

    private SimpleTextParsingRepositoryAdapter repository;

    @BeforeEach
    void setUp() {
        repository = new SimpleTextParsingRepositoryAdapter();
    }

    @Test
    @DisplayName("Test 1: Should parse comma-separated ingredients")
    void test1_shouldParseCommaSeparatedIngredients() {
        // Given
        String text = "tomate, cebolla, ajo";

        // When
        List<Ingredient> result = repository.execute(text);

        // Then
        assertEquals(3, result.size());
        assertEquals("tomate", result.get(0).getName());
        assertEquals("text", result.get(0).getSource());
        assertFalse(result.get(0).isConfirmed());

        assertEquals("cebolla", result.get(1).getName());
        assertEquals("ajo", result.get(2).getName());
    }

    @Test
    @DisplayName("Test 2: Should handle empty text")
    void test2_shouldHandleEmptyText() {
        // Given
        String text = "";

        // When
        List<Ingredient> result = repository.execute(text);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Test 3: Should trim whitespace")
    void test3_shouldTrimWhitespace() {
        // Given
        String text = " tomate , cebolla  , ajo ";

        // When
        List<Ingredient> result = repository.execute(text);

        // Then
        assertEquals(3, result.size());
        assertEquals("tomate", result.get(0).getName());
        assertEquals("cebolla", result.get(1).getName());
        assertEquals("ajo", result.get(2).getName());
    }
}