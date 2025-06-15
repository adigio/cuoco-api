package com.cuoco.adapter.out.rest.gemini;

import com.cuoco.adapter.out.rest.gemini.model.wrapper.ContentGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.GenerationConfigurationGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.InlineDataGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.PartGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.PromptBodyGeminiRequestModel;
import com.cuoco.adapter.out.rest.model.gemini.GeminiResponseMapper;
import com.cuoco.application.port.out.GetIngredientsFromImageRepository;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.shared.FileReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class GetIngredientsFromImageGeminiRestImageRepositoryAdapter implements GetIngredientsFromImageRepository {

    private final String PROMPT = FileReader.execute("prompt/recognizeIngredientsFromImage.txt");

    @Value("${gemini.api.url}")
    private String url;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.temperature}")
    private Double temperature;

    private final RestTemplate restTemplate;
    private final GeminiResponseMapper geminiResponseMapper;

    public GetIngredientsFromImageGeminiRestImageRepositoryAdapter(RestTemplate restTemplate, GeminiResponseMapper geminiResponseMapper) {
        this.restTemplate = restTemplate;
        this.geminiResponseMapper = geminiResponseMapper;
    }

    @Override
    public List<Ingredient> execute(List<MultipartFile> files) {

        log.info("Executing get ingredients from gemini rest adapter with files: {}", files);

        List<Ingredient> allIngredients = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                String imageBase64 = Base64.getEncoder().encodeToString(file.getBytes());
                String mimeType = file.getContentType() != null ? file.getContentType() : "image/jpeg";

                PromptBodyGeminiRequestModel prompt = buildPromptBody(imageBase64, mimeType, PROMPT);

                String geminiUrl = url + "?key=" + apiKey;

                String response = restTemplate.postForObject(geminiUrl, prompt, String.class);

                List<Ingredient> ingredients = parseIngredientsFromResponse(response);
                allIngredients.addAll(ingredients);

            } catch (Exception e) {
                log.error("Error processing file {}: {}", file.getOriginalFilename(), e.getMessage(), e);
            }
        }

        log.info("Successfully extracted ingredients from Gemini");

        return allIngredients;
    }

    @Override
    public Map<String, List<Ingredient>> executeWithSeparation(List<MultipartFile> files) {
        log.info("Executing get ingredients from gemini rest adapter with separation for {} files", files.size());

        Map<String, List<Ingredient>> ingredientsByImage = new LinkedHashMap<>();

        for (MultipartFile file : files) {
            try {
                String filename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "unknown_" + System.currentTimeMillis();

                String imageBase64 = Base64.getEncoder().encodeToString(file.getBytes());
                String mimeType = file.getContentType() != null ? file.getContentType() : "image/jpeg";

                PromptBodyGeminiRequestModel prompt = buildPromptBody(imageBase64, mimeType, PROMPT);

                String geminiUrl = url + "?key=" + apiKey;

                String response = restTemplate.postForObject(geminiUrl, prompt, String.class);

                List<Ingredient> ingredients = parseIngredientsFromResponse(response);
                ingredientsByImage.put(filename, ingredients);

                log.info("Extracted {} ingredients from file: {}", ingredients.size(), filename);

            } catch (Exception e) {
                String filename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "unknown";
                log.error("Error processing file {}: {}", filename, e.getMessage(), e);
                ingredientsByImage.put(filename, new ArrayList<>());
            }
        }

        log.info("Successfully extracted ingredients from {} images", ingredientsByImage.size());
        return ingredientsByImage;
    }

    private PromptBodyGeminiRequestModel buildPromptBody(String imageBase64, String mimeType, String prompt) {
        return PromptBodyGeminiRequestModel.builder()
                .contents(List.of(ContentGeminiRequestModel.builder().parts(buildPartsRequest(imageBase64, mimeType, prompt)).build()))
                .generationConfig(GenerationConfigurationGeminiRequestModel.builder().temperature(temperature).build())
                .build();
    }

    private List<PartGeminiRequestModel> buildPartsRequest(String imageBase64, String mimeType, String prompt) {
            return List.of(
                    PartGeminiRequestModel.builder()
                            .text(prompt)
                            .build(),
                    PartGeminiRequestModel.builder()
                            .inlineData(buildInlineData(imageBase64, mimeType))
                            .build()
            );
    }

    private InlineDataGeminiRequestModel buildInlineData(String imageBase64, String mimeType) {
        return InlineDataGeminiRequestModel.builder()
                .mimeType(mimeType)
                .data(imageBase64)
                .build();
    }

    private List<Ingredient> parseIngredientsFromResponse(String response) {
        if (response == null || response.trim().isEmpty()) {
            return new ArrayList<>();
        }

        // Usar el GeminiResponseMapper para extraer el texto
        String extractedText = geminiResponseMapper.extractTextFromResponse(response);

        if (extractedText != null && !extractedText.trim().isEmpty()) {
            return parseIngredientNamesFromText(extractedText);
        }

        // Fallback: parsear como string simple
        return parseIngredientNamesFromText(response);
    }

    private List<Ingredient> parseIngredientNamesFromText(String text) {
        List<Ingredient> ingredients = new ArrayList<>();
        String[] ingredientNames = text.split(",");

        for (String name : ingredientNames) {
            String cleanName = name.trim().toLowerCase();
            if (!cleanName.isEmpty()) {
                ingredients.add(Ingredient.builder()
                        .name(cleanName)
                        .source("image")
                        .confirmed(false)
                        .build()
                );
            }
        }

        return ingredients;
    }
}