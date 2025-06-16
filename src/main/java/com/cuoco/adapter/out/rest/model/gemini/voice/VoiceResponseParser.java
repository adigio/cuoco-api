package com.cuoco.adapter.out.rest.model.gemini.voice;

import com.cuoco.adapter.out.rest.model.gemini.GeminiResponseMapper;
import com.cuoco.application.usecase.model.Ingredient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class VoiceResponseParser {

    private final GeminiResponseMapper geminiResponseMapper;

    public VoiceResponseParser(GeminiResponseMapper geminiResponseMapper) {
        this.geminiResponseMapper = geminiResponseMapper;
    }

    /**
     * Parsear respuesta de Gemini y extraer ingredientes
     */
    public List<Ingredient> parseIngredientsFromResponse(String response) {
        if (response == null || response.trim().isEmpty()) {
            log.warn("Empty response from Gemini");
            return new ArrayList<>();
        }

        try {
            // Usar mapper existente para extraer texto de JSON
            String extractedText = geminiResponseMapper.extractTextFromResponse(response);

            if (extractedText != null && !extractedText.trim().isEmpty()) {
                return parseIngredientNamesFromText(extractedText);
            }

            log.warn("No text extracted from Gemini response");
            return new ArrayList<>();

        } catch (Exception e) {
            log.error("Error parsing Gemini response: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Parsear texto a lista de ingredientes con filtrado
     */
    private List<Ingredient> parseIngredientNamesFromText(String text) {
        List<Ingredient> ingredients = new ArrayList<>();
        String[] ingredientNames = text.split("[,\n]");

        for (String name : ingredientNames) {
            String cleanName = name.trim().toLowerCase();

            if (!cleanName.isEmpty()) {
                ingredients.add(
                        Ingredient.builder()
                                .name(cleanName)
                                .source("voice")
                                .confirmed(false)
                                .build());
            }
        }

        log.info("Parsed {} ingredients from text", ingredients.size());
        return ingredients;
    }
}