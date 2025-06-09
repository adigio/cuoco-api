package com.cuoco.adapter.out.rest.model.gemini.voice;

import com.cuoco.adapter.out.rest.model.gemini.GeminiResponseMapper;
import com.cuoco.application.usecase.model.Ingredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VoiceResponseParser {

    private static final Logger log = LoggerFactory.getLogger(VoiceResponseParser.class);

    private final GeminiResponseMapper geminiResponseMapper;

    public VoiceResponseParser(GeminiResponseMapper geminiResponseMapper) {
        this.geminiResponseMapper = geminiResponseMapper;
    }

    /**
     * Parsear respuesta de Gemini y extraer ingredientes
     */
    public List<Ingredient> parseIngredientsFromResponse(String response) {
        if (response == null || response.trim().isEmpty()) {
            log.warn("⚠️ Empty response from Gemini");
            return new ArrayList<>();
        }

        try {
            // Usar mapper existente para extraer texto de JSON
            String extractedText = geminiResponseMapper.extractTextFromResponse(response);

            if (extractedText != null && !extractedText.trim().isEmpty()) {
                return parseIngredientNamesFromText(extractedText);
            }

            log.warn("⚠️ No text extracted from Gemini response");
            return new ArrayList<>();

        } catch (Exception e) {
            log.error("❌ Error parsing Gemini response: {}", e.getMessage(), e);
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

            // Filtrar nombres muy cortos o inválidos
            if (!cleanName.isEmpty() && cleanName.length() > 1) {
                ingredients.add(new Ingredient(cleanName, "voz", false));
            }
        }

        log.info("📝 Parsed {} ingredients from text", ingredients.size());
        return ingredients;
    }
}