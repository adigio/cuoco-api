package com.cuoco.adapter.out.rest.gemini;

import com.cuoco.adapter.exception.UnprocessableException;
import com.cuoco.adapter.out.rest.gemini.model.MealPrepResponseGeminiModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.ContentGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.GeminiResponseModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.GenerationConfigurationGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.PartGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.PromptBodyGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.utils.Constants;
import com.cuoco.adapter.out.rest.gemini.utils.Utils;
import com.cuoco.application.port.out.GetMealPrepsFromIngredientsRepository;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.MealPrep;
import com.cuoco.application.usecase.model.MealPrepFilter;
import com.cuoco.shared.FileReader;
import com.cuoco.shared.model.ErrorDescription;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@Qualifier("provider")
public class GetMealPrepsFromIngredientsGeminiRestRepositoryAdapter implements GetMealPrepsFromIngredientsRepository {

    private final String DELIMITER = com.cuoco.shared.utils.Constants.COMMA.getValue();
    private final String EMPTY_STRING = com.cuoco.shared.utils.Constants.EMPTY.getValue();

    private final String BASIC_PROMPT = FileReader.execute("prompt/generateMealPrep/generateMealPrepFromIngredientsHeaderPrompt.txt");
    private final String FILTERS_PROMPT = FileReader.execute("prompt/generateMealPrep/generateMealPrepFiltersPrompt.txt");

    @Value("${gemini.api.url}")
    private String url;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.temperature}")
    private Double temperature;

    private final RestTemplate restTemplate;

    public GetMealPrepsFromIngredientsGeminiRestRepositoryAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<MealPrep> execute(MealPrep mealPrep) {
        try {
            log.info("Executing meal prep generation from Gemini with ingredients: {}", mealPrep.getIngredients());

            String ingredientNames = mealPrep.getIngredients()
                    .stream()
                    .map(Ingredient::getName)
                    .collect(Collectors.joining(","));

            String basicPrompt = BASIC_PROMPT
                    .replace(Constants.INGREDIENTS.getValue(), ingredientNames)
                    .replace(Constants.MAX_MEAL_PREPS.getValue(), mealPrep.getFilters().getServings().toString());

            String filtersPrompt = buildFiltersPrompt(mealPrep.getFilters());
            String finalPrompt =  basicPrompt.concat(filtersPrompt);

            PromptBodyGeminiRequestModel prompt = buildPromptBody(finalPrompt);

            String geminiUrl = url + "?key=" + apiKey;

            GeminiResponseModel response = restTemplate.postForObject(geminiUrl, prompt, GeminiResponseModel.class);
            log.info("Received response from Gemini.");

            if (response == null) {
                throw new UnprocessableException(ErrorDescription.NOT_AVAILABLE.getValue());
            }

            String sanitizedResponse = Utils.sanitizeJsonResponse(response);
            ObjectMapper mapper = new ObjectMapper();
            List<MealPrepResponseGeminiModel> mealPrepResponses = mapper.readValue(
                    sanitizedResponse,
                    new TypeReference<>() {}
            );

            List<MealPrep> mealPreps = mealPrepResponses.stream()
                    .map(MealPrepResponseGeminiModel::toDomain)
                    .collect(Collectors.toList());

            log.info("Generated {} meal preps from Gemini successfully", mealPreps.size());

            return mealPreps;
        } catch (Exception e) {
            log.error("Error generating meal preps from Gemini", e);
            throw new RuntimeException("Failed to generate meal preps");
        }
    }

    private String buildFiltersPrompt(MealPrepFilter filters) {
        if (filters == null) return null;

        String preparationTimeId = EMPTY_STRING;
        String cookLevelId = EMPTY_STRING;
        String dietId = EMPTY_STRING;
        String mealTypesIds = EMPTY_STRING;
        String allergiesIds = EMPTY_STRING;
        String dietaryNeedsIds = EMPTY_STRING;

        if(filters.getPreparationTime() != null && filters.getPreparationTime().getId() != null) {
            preparationTimeId = filters.getPreparationTime().getId().toString();
        }

        if(filters.getCookLevel() != null && filters.getCookLevel().getId() != null) {
            cookLevelId = filters.getCookLevel().getId().toString();
        }

        if(filters.getDiet() != null && filters.getDiet().getId() != null) {
            dietId = filters.getDiet().getId().toString();
        }

        if(filters.getCookLevel() != null && filters.getCookLevel().getId() != null) {
            cookLevelId = filters.getCookLevel().getId().toString();
        }

        if(filters.getMealTypes() != null && !filters.getMealTypes().isEmpty()) {
            mealTypesIds = filters.getMealTypes().stream().map(mt -> mt.getId().toString()).collect(Collectors.joining(DELIMITER));
        }

        if(filters.getAllergies() != null && !filters.getAllergies().isEmpty()) {
            allergiesIds = filters.getAllergies().stream().map(a -> a.getId().toString()).collect(Collectors.joining(DELIMITER));
        }

        if(filters.getDietaryNeeds() != null && !filters.getDietaryNeeds().isEmpty()) {
            dietaryNeedsIds = filters.getDietaryNeeds().stream().map(dn -> dn.getId().toString()).collect(Collectors.joining(DELIMITER));
        }

        String freeze = filters.getFreeze() != null ? filters.getFreeze().toString() : EMPTY_STRING;

        return FILTERS_PROMPT
                .replace(Constants.PREPARATION_TIME.getValue(), preparationTimeId)
                .replace(Constants.COOK_LEVEL.getValue(), cookLevelId)
                .replace(Constants.DIET.getValue(), dietId)
                .replace(Constants.MEAL_TYPES.getValue(), mealTypesIds)
                .replace(Constants.ALLERGIES.getValue(), allergiesIds)
                .replace(Constants.FREEZE.getValue(), freeze)
                .replace(Constants.DIETARY_NEEDS.getValue(), dietaryNeedsIds);
    }

    private PromptBodyGeminiRequestModel buildPromptBody(String prompt) {
        return PromptBodyGeminiRequestModel.builder()
                .contents(List.of(ContentGeminiRequestModel.builder().parts(buildPartsRequest(prompt)).build()))
                .generationConfig(GenerationConfigurationGeminiRequestModel.builder().temperature(temperature).build())
                .build();
    }

    private List<PartGeminiRequestModel> buildPartsRequest(String prompt) {
        return List.of(PartGeminiRequestModel.builder().text(prompt).build());
    }
}
