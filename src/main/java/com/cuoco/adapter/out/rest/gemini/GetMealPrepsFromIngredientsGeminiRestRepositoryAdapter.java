package com.cuoco.adapter.out.rest.gemini;

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

            log.info("Building basic prompt...");
            String basicPrompt = BASIC_PROMPT
                    .replace(Constants.INGREDIENTS.getValue(), ingredientNames)
                    .replace(Constants.MAX_MEAL_PREPS.getValue(), mealPrep.getFilters().getQuantity().toString());

            String filtersPrompt = buildFiltersPrompt(mealPrep.getFilters());
            log.info("Filters prompt built: {}", filtersPrompt);

            String finalPrompt =  basicPrompt.concat(filtersPrompt);
            log.info("Final prompt: {}", finalPrompt);
            PromptBodyGeminiRequestModel prompt = buildPromptBody(finalPrompt);
            log.info("Prompt body created.");

            String geminiUrl = url + "?key=" + apiKey;

            GeminiResponseModel response = restTemplate.postForObject(geminiUrl, prompt, GeminiResponseModel.class);
            log.info("Received response from Gemini.");


            if (response == null) {
                throw new RuntimeException("Gemini response is null");
            }

            String sanitizedResponse = Utils.sanitizeJsonResponse(response);
            log.info("Sanitized response: {}", sanitizedResponse);

            ObjectMapper mapper = new ObjectMapper();
            log.info("Mapped response to domain objects.");
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

        String types = filters.getTypes() != null ? String.join(",", filters.getTypes()) : "";

        return FILTERS_PROMPT
                .replace(Constants.QUANTITY.getValue(), filters.getQuantity() != null ? filters.getQuantity().toString() : "1")
                .replace(Constants.COOK_LEVEL.getValue(), filters.getDifficulty() != null ? filters.getDifficulty().toString() : "")
                .replace(Constants.DIET.getValue(), filters.getDiet() != null ? filters.getDiet() : "")
                .replace(Constants.FREEZE.getValue(), filters.getFreeze() != null ? filters.getFreeze().toString() : "")
                .replace(Constants.MEAL_TYPES.getValue(), types);
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
