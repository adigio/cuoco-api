package com.cuoco.adapter.out.rest.gemini;

import autovalue.shaded.kotlin.Pair;
import com.cuoco.adapter.exception.NotAvailableException;
import com.cuoco.adapter.exception.UnprocessableException;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.GeminiResponseModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.PromptBodyGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.utils.Constants;
import com.cuoco.adapter.out.rest.gemini.utils.ImageUtils;
import com.cuoco.application.port.out.GetRecipeStepsImagesRepository;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.Step;
import com.cuoco.shared.FileReader;
import com.cuoco.shared.model.ErrorDescription;
import com.cuoco.shared.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.cuoco.shared.utils.ImageConstants.STEP_INFIX;

@Slf4j
@Repository
public class GetRecipeStepsImagesGeminiRestRepositoryAdapter implements GetRecipeStepsImagesRepository {

    private final String STEP_IMAGE_PROMPT = FileReader.execute("prompt/generateimages/generateStepImagePrompt.txt");

    @Value("${gemini.image.url}")
    private String imageUrl;

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ImageUtils imageUtils;

    public GetRecipeStepsImagesGeminiRestRepositoryAdapter(RestTemplate restTemplate, ImageUtils imageUtils) {
        this.restTemplate = restTemplate;
        this.imageUtils = imageUtils;
    }

    @Override
    public List<Step> execute(Recipe recipe) {
        log.info("Generating steps images for recipe with ID {}", recipe.getId());

        List<Step> imagesCreated = new ArrayList<>();

        try {
            List<Step> stepImages = recipe.getImages().stream().map(stepImage -> buildStepImage(recipe.getId(), stepImage)).toList();

            if(!stepImages.isEmpty()) {
                imagesCreated.addAll(stepImages);
            }

            log.info("Generated {} steps images for recipe with ID {}", imagesCreated.size(), recipe.getId());

            return imagesCreated;

        } catch (Exception e) {
            log.error("Error generating steps images for recipe: {}", recipe.getName(), e);
            throw new NotAvailableException("Could not generate steps images for recipe: " + recipe.getName());
        }
    }

    private Step buildStepImage(Long recipeId, Step stepImage) {
        String prompt = STEP_IMAGE_PROMPT.replace(Constants.STEP_INSTRUCTION.getValue(), stepImage.getDescription());

        Pair<String, byte[]> imageData = sendToGemini(prompt);

        if (imageData != null) {
            log.info("Recipe ID {}: Generated image for step {}. Saving file in recipe folder.", recipeId, stepImage.getNumber());

            String imageName = recipeId + STEP_INFIX.getValue() + stepImage.getNumber() + FileUtils.getImageFormat(imageData.getFirst());

            imageUtils.saveImageFile(imageData.getSecond(), recipeId, imageName);

            stepImage.setImageName(imageName);

            return stepImage;
        } else {
            log.warn("Failed to create step {} image for recipe with ID {}. Skipping", stepImage.getNumber(), recipeId);
            return null;
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