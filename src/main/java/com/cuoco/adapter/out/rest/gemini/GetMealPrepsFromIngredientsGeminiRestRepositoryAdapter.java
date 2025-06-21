package com.cuoco.adapter.out.rest.gemini;

import com.fasterxml.jackson.core.type.TypeReference;
import com.cuoco.adapter.out.rest.gemini.model.MealPrepResponseGeminiModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.*;
import com.cuoco.adapter.out.rest.gemini.utils.Utils;
import com.cuoco.application.port.out.GetMealPrepsFromIngredientsRepository;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.MealPrep;
import com.cuoco.application.usecase.model.MealPrepFilter;
import com.cuoco.shared.FileReader;
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

            String basicPrompt = BASIC_PROMPT
                    .replace("{ingredients}", ingredientNames)
                    .replace("{max_meal_preps}", mealPrep.getFilters().getQuantity().toString());

            String filtersPrompt = buildFiltersPrompt(mealPrep.getFilters());

            String finalPrompt =  basicPrompt.concat(filtersPrompt);

            PromptBodyGeminiRequestModel prompt = buildPromptBody(finalPrompt);

            String geminiUrl = url + "?key=" + apiKey;

            GeminiResponseModel response = restTemplate.postForObject(geminiUrl, prompt, GeminiResponseModel.class);

            if (response == null) {
                throw new RuntimeException("Gemini response is null");
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

        String types = filters.getTypes() != null ? String.join(",", filters.getTypes()) : "";

        return FILTERS_PROMPT
                .replace("{QUANTITY}", filters.getQuantity() != null ? filters.getQuantity().toString() : "1")
                .replace("{COOK_LEVEL}", filters.getDifficulty() != null ? filters.getDifficulty().toString() : "")
                .replace("{DIET}", filters.getDiet() != null ? filters.getDiet() : "")
                .replace("{FREEZE}", filters.getFreeze() != null ? filters.getFreeze().toString() : "")
                .replace("{FOOD_TYPES}", types);
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
