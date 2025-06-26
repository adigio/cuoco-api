package com.cuoco.adapter.out.rest.gemini;

import autovalue.shaded.kotlin.Pair;
import com.cuoco.adapter.exception.NotAvailableException;
import com.cuoco.adapter.exception.UnprocessableException;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.GeminiResponseModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.PromptBodyGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.utils.Constants;
import com.cuoco.adapter.out.rest.gemini.utils.ImageUtils;
import com.cuoco.application.port.out.GenerateRecipeMainImageRepository;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.shared.FileReader;
import com.cuoco.shared.model.ErrorDescription;
import com.cuoco.shared.utils.FileUtils;
import com.cuoco.shared.utils.ImageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;

@Slf4j
@Repository
public class GetRecipeMainImageGeminiRestRepositoryAdapter implements GenerateRecipeMainImageRepository {

    private final String DELIMITER = com.cuoco.shared.utils.Constants.COMMA.getValue();

    private final String MAIN_IMAGE_PROMPT = FileReader.execute("prompt/generateimages/generateRecipeImagePrompt.txt");

    @Value("${gemini.image.url}")
    private String imageUrl;

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ImageUtils imageUtils;

    public GetRecipeMainImageGeminiRestRepositoryAdapter(RestTemplate restTemplate, ImageUtils imageUtils) {
        this.restTemplate = restTemplate;
        this.imageUtils = imageUtils;
    }

    @Override
    public void execute(Recipe recipe) {
        log.info("Generating main image for recipe with ID {}", recipe.getId());

        if(imageUtils.imageExists(recipe.getId(), ImageConstants.MAIN_IMAGE_NAME.getValue())) {
            log.info("Main image for recipe with ID {} already exists", recipe.getId());
            return;
        }

        try {
            int maxIngredients = Integer.parseInt(ImageConstants.MAX_INGREDIENTS_SIZE_INT.getValue());

            String mainIngredients = recipe.getIngredients().stream()
                    .map(Ingredient::getName)
                    .limit(maxIngredients)
                    .collect(Collectors.joining(DELIMITER));

            String prompt = MAIN_IMAGE_PROMPT
                    .replace(Constants.RECIPE_NAME.getValue(), recipe.getName())
                    .replace(Constants.MAIN_INGREDIENTS.getValue(), mainIngredients);

            Pair<String, byte[]> imageData = sendToGemini(prompt);

            if (imageData != null) {
                log.info("Generated main image. Saving file in recipe folder.");

                String imageName = ImageConstants.MAIN_IMAGE_NAME.getValue() + FileUtils.getImageFormat(imageData.getFirst());

                imageUtils.saveImageFile(imageData.getSecond(), recipe.getId(), imageName);

                log.info("Successfully generated and saved main image for recipe with ID {}", recipe.getId());

            } else log.warn("Failed to generate main image for recipe with ID {}", recipe.getId());

        } catch (Exception e) {
            log.error("Error generating images for recipe with ID {}", recipe.getId(), e);
            throw new NotAvailableException("Could not generate main image for recipe with ID " + recipe.getId());
        }
    }

    private Pair<String, byte[]> sendToGemini(String prompt) {
        try {
            PromptBodyGeminiRequestModel requestBody = imageUtils.buildPromptBody(prompt);

            String geminiUrl = imageUrl + "?key=" + apiKey;
            GeminiResponseModel response = restTemplate.postForObject(geminiUrl, requestBody, GeminiResponseModel.class);

            if (response == null) {
                throw new UnprocessableException(ErrorDescription.NOT_AVAILABLE.getValue());
            }

            return imageUtils.extractImageFromResponse(response);

        } catch (Exception e) {
            log.error("Error calling Gemini image generation API: ", e);
            return null;
        }
    }
}