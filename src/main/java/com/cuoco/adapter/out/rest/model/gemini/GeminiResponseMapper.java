package com.cuoco.adapter.out.rest.model.gemini;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class GeminiResponseMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Object parseToJson(String geminiResponse) {
        return extractText(geminiResponse)
                .map(text -> text.replace("```json", "").replace("```", "").trim())
                .map(this::parseJsonSafely)
                .orElse(parseJsonSafely(geminiResponse));
    }

    public String extractTextFromResponse(String geminiResponse) {
        return extractText(geminiResponse).orElse(geminiResponse);
    }

    public Object parseToJsonArray(String geminiResponse) {
        return parseJsonSafely(geminiResponse);
    }

    private Optional<String> extractText(String geminiResponse) {
        try {
            JsonNode root = objectMapper.readTree(geminiResponse);
            return Optional.ofNullable(root.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText(null));
        } catch (Exception e) {
            log.warn("Failed to extract text: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private Object parseJsonSafely(String json) {
        try {
            return objectMapper.readValue(json, Object.class);
        } catch (Exception e) {
            return json;
        }
    }
}