package com.cuoco.adapter.out.rest.gemini;

import com.cuoco.adapter.exception.NotAvailableException;
import com.cuoco.adapter.exception.UnprocessableException;
import com.cuoco.adapter.out.hibernate.GetAllAllergiesDatabaseRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.GetAllCookLevelsDatabaseRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.GetAllDietaryNeedsDatabaseRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.GetAllDietsDatabaseRepositoryAdapter;
import com.cuoco.adapter.out.rest.gemini.model.RecipeResponseGeminiModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.ContentGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.GeminiResponseModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.GenerationConfigurationGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.PartGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.PromptBodyGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.utils.Constants;
import com.cuoco.adapter.out.rest.gemini.utils.Utils;
import com.cuoco.application.port.out.GetAllAllergiesRepository;
import com.cuoco.application.port.out.GetAllCookLevelsRepository;
import com.cuoco.application.port.out.GetAllDietaryNeedsRepository;
import com.cuoco.application.port.out.GetAllDietsRepository;
import com.cuoco.application.port.out.GetAllergiesByIdRepository;
import com.cuoco.application.port.out.GetRecipesFromIngredientsRepository;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.MealType;
import com.cuoco.application.usecase.model.ParametricData;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.RecipeFilter;
import com.cuoco.shared.FileReader;
import com.cuoco.shared.model.ErrorDescription;
import com.fasterxml.jackson.core.JsonProcessingException;
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
public class GetRecipesFromIngredientsGeminiRestRepositoryAdapter implements GetRecipesFromIngredientsRepository {

    private final String DELIMITER = com.cuoco.shared.utils.Constants.COMMA.getValue();
    private final String EMPTY_STRING = com.cuoco.shared.utils.Constants.EMPTY.getValue();

    private final String BASIC_PROMPT = FileReader.execute("prompt/generaterecipes/generateRecipeFromIngredientsHeaderPrompt.txt");
    private final String PARAMETRIC_PROMPT = FileReader.execute("prompt/generaterecipes/generateRecipeParametricDataPrompt.txt");
    private final String FILTERS_PROMPT = FileReader.execute("prompt/generaterecipes/generateRecipesFiltersPrompt.txt");

    @Value("${gemini.api.url}")
    private String url;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.temperature}")
    private Double temperature;

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public GetRecipesFromIngredientsGeminiRestRepositoryAdapter(ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Recipe> execute(Recipe recipe) {
        try {
            log.info("Executing recipes generation from Gemini rest adapter with ingredients: {}", recipe.getIngredients());

            String ingredientNames = recipe.getIngredients().stream().map(Ingredient::getName).collect(Collectors.joining(DELIMITER));

            String basicPrompt = BASIC_PROMPT
                    .replace(Constants.INGREDIENTS.getValue(), ingredientNames)
                    .replace(Constants.MAX_RECIPES.getValue(), recipe.getConfiguration().getSize().toString())
                    .concat(buildParametricPrompt(recipe.getConfiguration().getParametricData()));

            String filtersPrompt = buildFiltersPrompt(recipe.getFilters());
            String finalPrompt = filtersPrompt == null ? basicPrompt : basicPrompt.concat(filtersPrompt);

            PromptBodyGeminiRequestModel promptBody = buildPromptBody(finalPrompt);

            String geminiUrl = url + "?key=" + apiKey;
            GeminiResponseModel response = restTemplate.postForObject(geminiUrl, promptBody, GeminiResponseModel.class);

            if(response == null) {
                throw new UnprocessableException(ErrorDescription.NOT_AVAILABLE.getValue());
            }

            String recipeResponseText = Utils.sanitizeJsonResponse(response);

            List<RecipeResponseGeminiModel> recipesResponseFromGemini = objectMapper.readValue(
                    recipeResponseText,
                    new TypeReference<>() {}
            );

            List<Recipe> recipesResponse = recipesResponseFromGemini.stream()
                    .map(RecipeResponseGeminiModel::toDomain)
                    .toList();

            log.info("Generated {} recipes from Gemini successfully", recipesResponse.size());

            return recipesResponse;
        } catch (Exception e) {
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

    private String buildFiltersPrompt(RecipeFilter filters) {

        if(filters.getEnable()) {

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

            if(filters.getTypes() != null && !filters.getTypes().isEmpty()) {
                mealTypesIds = filters.getTypes().stream().map(mt -> mt.getId().toString()).collect(Collectors.joining(DELIMITER));
            }

            if(filters.getAllergies() != null && !filters.getAllergies().isEmpty()) {
                allergiesIds = filters.getAllergies().stream().map(a -> a.getId().toString()).collect(Collectors.joining(DELIMITER));
            }

            if(filters.getDietaryNeeds() != null && !filters.getDietaryNeeds().isEmpty()) {
                dietaryNeedsIds = filters.getDietaryNeeds().stream().map(dn -> dn.getId().toString()).collect(Collectors.joining(DELIMITER));
            }

            return FILTERS_PROMPT
                    .replace(Constants.PREPARATION_TIME.getValue(), preparationTimeId)
                    .replace(Constants.COOK_LEVEL.getValue(), cookLevelId)
                    .replace(Constants.DIET.getValue(), dietId)
                    .replace(Constants.MEAL_TYPES.getValue(), mealTypesIds)
                    .replace(Constants.ALLERGIES.getValue(), allergiesIds)
                    .replace(Constants.DIETARY_NEEDS.getValue(), dietaryNeedsIds);
        }

        return null;
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
