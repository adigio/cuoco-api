package com.cuoco.adapter.out.rest.model.gemini;

import com.cuoco.application.port.out.GetIngredientsFromRepository;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.shared.FileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class GeminiImageToIngredientsRepositoryAdapter implements GetIngredientsFromRepository {

    static final Logger log = LoggerFactory.getLogger(GeminiImageToIngredientsRepositoryAdapter.class);

    private final String PROMPT = FileReader.execute("prompt/generateIngredients.txt");

    @Value("${gemini.api.url}")
    private String url;

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final GeminiResponseMapper geminiResponseMapper;

    public GeminiImageToIngredientsRepositoryAdapter(RestTemplate restTemplate, GeminiResponseMapper geminiResponseMapper) {
        this.restTemplate = restTemplate;
        this.geminiResponseMapper = geminiResponseMapper;
    }

    @Override
    public List<Ingredient> execute(List<MultipartFile> files) {

        log.info("Executing get ingredients from gemini rest adapter with files: {}", files);

        List<Ingredient> allIngredients = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                PromptBodyGeminiRequestModel prompt = buildPromptBody(file);

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

                PromptBodyGeminiRequestModel prompt = buildPromptBody(file);

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

    private PromptBodyGeminiRequestModel buildPromptBody(MultipartFile file) {
        return new PromptBodyGeminiRequestModel(
                buildContentRequest(file),
                new GenerationConfigurationGeminiRequestModel(
                        0.2
                )
        );
    }

    private List<ContentGeminiRequestModel> buildContentRequest(MultipartFile file) {
        return List.of(
                new ContentGeminiRequestModel(buildPartsRequest(file))
        );
    }

    private List<PartGeminiRequestModel> buildPartsRequest(MultipartFile file) {
        try {
            String imageBase64 = Base64.getEncoder().encodeToString(file.getBytes());
            String mimeType = file.getContentType();

            return List.of(
                    new PartGeminiRequestModel(
                            null,
                            PROMPT
                    ),
                    new PartGeminiRequestModel(
                            new InlineDataGeminiRequestModel(
                                    mimeType != null ? mimeType : "image/jpeg",
                                    imageBase64
                            ),
                            null
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException("Error processing image file: " + e.getMessage(), e);
        }
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
                ingredients.add(new Ingredient(cleanName, "imagen", false));
            }
        }

        return ingredients;
    }
}