package com.cuoco.adapter.out.rest.gemini;

import com.cuoco.adapter.exception.NotAvailableException;
import com.cuoco.application.port.out.GenerateRecipeImagesRepository;
import com.cuoco.application.usecase.domainservice.ImageDomainService;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.RecipeImage;
import com.cuoco.shared.FileReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class GetRecipeImagesGeminiRestRepositoryAdapter implements GenerateRecipeImagesRepository {

    private final String MAIN_IMAGE_PROMPT = FileReader.execute("prompt/generateimages/generateRecipeImagePrompt.txt");
    private final String STEP_IMAGE_PROMPT = FileReader.execute("prompt/generateimages/generateStepImagePrompt.txt");

    @Value("${gemini.image.url}")
    private String imageUrl;

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ImageDomainService imageDomainService;

    public GetRecipeImagesGeminiRestRepositoryAdapter(RestTemplate restTemplate, ImageDomainService imageDomainService) {
        this.restTemplate = restTemplate;
        this.imageDomainService = imageDomainService;
    }

    @Override
    public List<RecipeImage> execute(Recipe recipe) {
        log.info("Generating images for recipe: {}", recipe.getName());
        List<RecipeImage> images = new ArrayList<>();

        try {
            RecipeImage mainImage = buildMainRecipeImage(recipe);
            if (mainImage != null) {
                images.add(mainImage);
            }

            List<RecipeImage> stepImages = buildStepImages(recipe);
            images.addAll(stepImages);

            log.info("Generated {} images for recipe: {}", images.size(), recipe.getName());
            return images;

        } catch (Exception e) {
            log.error("Error generating images for recipe: {}", recipe.getName(), e);
            throw new NotAvailableException("Could not generate images for recipe: " + recipe.getName());
        }
    }

    private RecipeImage buildMainRecipeImage(Recipe recipe) {
        try {
            String mainIngredients = recipe.getIngredients().stream()
                    .limit(5)
                    .map(ingredient -> ingredient.getName())
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");

            String prompt = MAIN_IMAGE_PROMPT
                    .replace("{RECIPE_NAME}", recipe.getName())
                    .replace("{MAIN_INGREDIENTS}", mainIngredients);

            byte[] imageData = buildImageFromGemini(prompt);

            if (imageData != null) {
                String sanitizedName = imageDomainService.sanitizeRecipeName(recipe.getName());
                String imageName = imageDomainService.buildMainImageName(sanitizedName);
                String imagePath = imageDomainService.buildMainImagePath(sanitizedName);
                String imageUrl = imageDomainService.buildMainImageUrl(sanitizedName, imageName);
                String fullPath = imageDomainService.saveImageToFile(imagePath, imageName, imageData);

                return RecipeImage.builder()
                        .imageName(imageName)
                        .imageUrl(imageUrl)
                        .imagePath(fullPath)
                        .imageType("main")
                        .build();
            }
        } catch (Exception e) {
            log.error("Error generating main image for recipe: {}", recipe.getName(), e);
        }
        return null;
    }

    private List<RecipeImage> buildStepImages(Recipe recipe) {
        List<RecipeImage> stepImages = new ArrayList<>();

        try {
            String[] instructions = recipe.getInstructions().split("\\d+\\.|\\n|\\r\\n|;");
            List<String> validInstructions = new ArrayList<>();

            // Filter out empty instructions and collect valid ones
            for (String instruction : instructions) {
                String trimmed = instruction.trim();
                if (!trimmed.isEmpty()) {
                    validInstructions.add(trimmed);
                }
            }

            int maxSteps = Math.min(validInstructions.size(), 3);

            for (int i = 0; i < maxSteps; i++) {
                String instruction = validInstructions.get(i);

                String prompt = STEP_IMAGE_PROMPT.replace("{STEP_INSTRUCTION}", instruction);
                byte[] imageData = buildImageFromGemini(prompt);

                if (imageData != null) {
                    String sanitizedName = imageDomainService.sanitizeRecipeName(recipe.getName());
                    String imageName = imageDomainService.buildStepImageName(sanitizedName, i + 1);
                    String imagePath = imageDomainService.buildStepImagePath(sanitizedName);
                    String imageUrl = imageDomainService.buildStepImageUrl(sanitizedName, imageName);
                    String fullPath = imageDomainService.saveImageToFile(imagePath, imageName, imageData);

                    stepImages.add(RecipeImage.builder()
                            .imageName(imageName)
                            .imageUrl(imageUrl)
                            .imagePath(fullPath)
                            .imageType("step")
                            .stepNumber(i + 1)
                            .stepDescription(instruction)
                            .build());
                }
            }
        } catch (Exception e) {
            log.error("Error generating step images for recipe: {}", recipe.getName(), e);
        }

        return stepImages;
    }

    private byte[] buildImageFromGemini(String prompt) {
        try {
            Map<String, Object> requestBody = buildPromptBody(prompt);
            String geminiUrl = imageUrl + "?key=" + apiKey;

            ResponseEntity<Map> response = restTemplate.postForEntity(geminiUrl, requestBody, Map.class);

            if (response.getBody() == null) {
                return null;
            }

            return extractImageFromResponse(response.getBody());

        } catch (Exception e) {
            log.error("Error calling Gemini image generation API: ", e);
            return null;
        }
    }

    private Map<String, Object> buildPromptBody(String prompt) {
        Map<String, Object> requestBody = new HashMap<>();

        List<Map<String, Object>> contents = new ArrayList<>();
        Map<String, Object> content = new HashMap<>();
        List<Map<String, Object>> parts = new ArrayList<>();
        Map<String, Object> textPart = new HashMap<>();
        textPart.put("text", prompt);
        parts.add(textPart);
        content.put("parts", parts);
        contents.add(content);
        requestBody.put("contents", contents);

        Map<String, Object> generationConfig = new HashMap<>();
        generationConfig.put("responseModalities", List.of("TEXT", "IMAGE"));
        requestBody.put("generationConfig", generationConfig);

        return requestBody;
    }

    private byte[] extractImageFromResponse(Map<String, Object> responseBody) {
        try {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");

            if (candidates == null || candidates.isEmpty()) {
                return null;
            }

            Map<String, Object> candidate = candidates.get(0);
            Map<String, Object> content = (Map<String, Object>) candidate.get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");

            for (Map<String, Object> part : parts) {
                if (part.containsKey("inlineData")) {
                    Map<String, Object> inlineData = (Map<String, Object>) part.get("inlineData");
                    String base64Data = (String) inlineData.get("data");

                    if (base64Data != null && !base64Data.trim().isEmpty()) {
                        return Base64.getDecoder().decode(base64Data);
                    }
                }
            }

            return null;
        } catch (Exception e) {
            log.error("Error extracting image from Gemini response", e);
            return null;
        }
    }
}