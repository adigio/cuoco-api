package com.cuoco.adapter.out.rest.gemini;

import com.cuoco.adapter.exception.NotAvailableException;
import com.cuoco.adapter.exception.UnprocessableException;
import com.cuoco.adapter.out.rest.gemini.model.RecipeResponseGeminiModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.ContentGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.GeminiResponseModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.GenerationConfigurationGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.PartGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.PromptBodyGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.utils.Utils;
import com.cuoco.application.port.out.GetRecipesFromIngredientsRepository;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.RecipeFilter;
import com.cuoco.adapter.out.rest.gemini.utils.Constants;
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
public class GetRecipesFromIngredientsGeminiRestRepositoryAdapter implements GetRecipesFromIngredientsRepository {

    private final String BASIC_PROMPT = FileReader.execute("prompt/generaterecipes/generateRecipeFromIngredientsHeaderPrompt.txt");
    private final String FILTERS_PROMPT = FileReader.execute("prompt/generaterecipes/generateRecipeFromIngredientsHeaderPrompt.txt");

    @Value("${gemini.api.url}")
    private String url;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.temperature}")
    private Double temperature;

    private final RestTemplate restTemplate;

    public GetRecipesFromIngredientsGeminiRestRepositoryAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Recipe> execute(Recipe recipe) {
        try {
            log.info("Executing recipes generation from Gemini rest adapter with ingredients: {}", recipe.getIngredients());

            String ingredientNames = recipe.getIngredients().stream().map(Ingredient::getName).collect(Collectors.joining(","));

            String basicPrompt = BASIC_PROMPT
                    .replace(Constants.INGREDIENTS.getValue(), ingredientNames)
                    .replace(Constants.MAX_RECIPES.getValue(), recipe.getFilters().getMaxRecipes().toString());

            String filtersPrompt = buildFiltersPrompt(recipe.getFilters());

            String finalPrompt = filtersPrompt == null ? basicPrompt : basicPrompt.concat(filtersPrompt);

            PromptBodyGeminiRequestModel prompt = buildPromptBody(finalPrompt);

            String geminiUrl = url + "?key=" + apiKey;

            GeminiResponseModel response = restTemplate.postForObject(geminiUrl, prompt, GeminiResponseModel.class);

            if(response == null) {
                throw new UnprocessableException(ErrorDescription.NOT_AVAILABLE.getValue());
            }

            String recipeResponseText = Utils.sanitizeJsonResponse(response);

            ObjectMapper mapper = new ObjectMapper();

            List<RecipeResponseGeminiModel> recipesResponseFromGemini = mapper.readValue(
                    recipeResponseText,
                    new TypeReference<>() {}
            );

            List<Recipe> recipesResponse = recipesResponseFromGemini.stream().map(RecipeResponseGeminiModel::toDomain).toList();

            log.info("Generated {} recipes from Gemini successfully", recipesResponse.size());

            return recipesResponse;
        } catch (Exception e) {
            log.error("Error getting recipes from ingredients in Gemini. ", e);
            throw new NotAvailableException(ErrorDescription.NOT_AVAILABLE.getValue());
        }
    }

    private String buildFiltersPrompt(RecipeFilter filters) {

        if(filters.getEnable()) {
            String foodTypes = String.join(",", filters.getTypes());

            return FILTERS_PROMPT
                    .replace(Constants.QUANTITY.getValue(), filters.getQuantity().toString())
                    .replace(Constants.COOK_LEVEL.getValue(), filters.getDifficulty().getDescription())
                    .replace(Constants.COOK_TIME.getValue(), filters.getTime())
                    .replace(Constants.FOOD_TYPES.getValue(), foodTypes)
                    .replace(Constants.DIET.getValue(), filters.getDiet());
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
