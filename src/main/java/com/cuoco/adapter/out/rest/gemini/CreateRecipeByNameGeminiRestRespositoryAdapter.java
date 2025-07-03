package com.cuoco.adapter.out.rest.gemini;

import com.cuoco.adapter.exception.NotAvailableException;
import com.cuoco.adapter.exception.UnprocessableException;
import com.cuoco.adapter.out.rest.gemini.model.RecipeResponseGeminiModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.GeminiResponseModel;
import com.cuoco.adapter.out.rest.gemini.utils.Constants;
import com.cuoco.adapter.out.rest.gemini.utils.Utils;
import com.cuoco.application.port.out.CreateRecipeByNameRepository;
import com.cuoco.application.usecase.model.ParametricData;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.shared.FileReader;
import com.cuoco.shared.model.ErrorDescription;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
public class CreateRecipeByNameGeminiRestRespositoryAdapter implements CreateRecipeByNameRepository {

    private final String BASIC_PROMPT = FileReader.execute("prompt/generaterecipes/generateRecipeFromNameHeaderPrompt.txt");
    private final String PARAMETRIC_PROMPT = FileReader.execute("prompt/generaterecipes/generateRecipeParametricDataPrompt.txt");

    @Value("${gemini.api.url}")
    private String url;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.temperature}")
    private Double temperature;

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public CreateRecipeByNameGeminiRestRespositoryAdapter(ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public Recipe execute(String name, ParametricData parametricData) {
        try {
            log.info("Executing quick recipe generation from Gemini rest adapter with name: {}", name);

            String basicPrompt = BASIC_PROMPT.replace(Constants.RECIPE_NAME.getValue(), name);

            String basicWithParametricPrompt = basicPrompt.concat(buildParametricPrompt(parametricData));

            String geminiUrl = url + "?key=" + apiKey;
            GeminiResponseModel response = restTemplate.postForObject(
                    geminiUrl,
                    Utils.buildPromptBody(basicWithParametricPrompt, temperature),
                    GeminiResponseModel.class
            );

            if(response == null) {
                throw new UnprocessableException(ErrorDescription.NOT_AVAILABLE.getValue());
            }

            String recipeResponseText = Utils.sanitizeJsonResponse(response);

            List<RecipeResponseGeminiModel> recipesResponseFromGemini = objectMapper.readValue(
                    recipeResponseText,
                    new TypeReference<>() {}
            );

            if(recipesResponseFromGemini.isEmpty()) {
                throw new UnprocessableException(ErrorDescription.NOT_AVAILABLE.getValue());
            }

            Recipe recipeResponse = recipesResponseFromGemini.get(0).toDomain();

            log.info("Generated recipe from Gemini successfully. Recipe: {}", recipeResponse);

            return recipeResponse;
        } catch (JsonProcessingException e) {
            log.error("Failed to convert some properties to JSON. ", e);
            throw new NotAvailableException(ErrorDescription.NOT_AVAILABLE.getValue());
        }catch (Exception e) {
            log.error("Error getting recipes from ingredients in Gemini. ", e);
            throw new NotAvailableException(ErrorDescription.NOT_AVAILABLE.getValue());
        }
    }

    private String buildParametricPrompt(ParametricData parametricData) throws JsonProcessingException {
        return PARAMETRIC_PROMPT
                .replace(Constants.PARAMETRIC_UNITS.getValue(), objectMapper.writeValueAsString(parametricData.getUnits()))
                .replace(Constants.PARAMETRIC_PREPARATION_TIMES.getValue(), objectMapper.writeValueAsString(parametricData.getPreparationTimes()))
                .replace(Constants.PARAMETRIC_COOK_LEVELS.getValue(), objectMapper.writeValueAsString(parametricData.getCookLevels()))
                .replace(Constants.PARAMETRIC_DIETS.getValue(), objectMapper.writeValueAsString(parametricData.getDiets()))
                .replace(Constants.PARAMETRIC_MEAL_TYPES.getValue(), objectMapper.writeValueAsString(parametricData.getMealTypes()))
                .replace(Constants.PARAMETRIC_ALLERGIES.getValue(), objectMapper.writeValueAsString(parametricData.getAllergies()))
                .replace(Constants.PARAMETRIC_DIETARY_NEEDS.getValue(), objectMapper.writeValueAsString(parametricData.getDietaryNeeds()));
    }
}
