package com.cuoco.adapter.out.rest.gemini;

import com.cuoco.adapter.exception.NotAvailableException;
import com.cuoco.adapter.exception.UnprocessableException;
import com.cuoco.adapter.out.rest.gemini.model.MealPrepResponseGeminiModel;
import com.cuoco.adapter.out.rest.gemini.model.RecipeRequestGeminiModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.ContentGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.GeminiResponseModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.GenerationConfigurationGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.PartGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.PromptBodyGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.utils.Constants;
import com.cuoco.adapter.out.rest.gemini.utils.Utils;
import com.cuoco.application.port.out.GetMealPrepsFromIngredientsRepository;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.MealPrep;
import com.cuoco.application.usecase.model.MealType;
import com.cuoco.application.usecase.model.Recipe;
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
public class GetMealPrepsFromIngredientsGeminiRestRepositoryAdapter implements GetMealPrepsFromIngredientsRepository {

    private final String BASIC_PROMPT = FileReader.execute("prompt/generateMealPrep/generateMealPrepFromIngredientsHeaderPrompt.txt");

    @Value("${gemini.api.url}")
    private String url;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.temperature}")
    private Double temperature;

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public GetMealPrepsFromIngredientsGeminiRestRepositoryAdapter(ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<MealPrep> execute(MealPrep mealPrep) {
        try {
            log.info("Executing meal prep generation from Gemini with ingredients: {}", mealPrep.getIngredients());

            String recipesJson = buildRecipesJson(mealPrep.getRecipes());
            String mealPrepsToNotInclude = buildMealPrepsToNotInclude(mealPrep.getConfiguration().getNotInclude());

            String basicPrompt = BASIC_PROMPT
                    .replace(Constants.RECIPES.getValue(), recipesJson)
                    .replace(Constants.NOT_INCLUDE.getValue(), mealPrepsToNotInclude)
                    .replace(Constants.MAX_MEAL_PREPS.getValue(), mealPrep.getFilters().getServings().toString())
                    .replace(Constants.FREEZE.getValue(), mealPrep.getFilters().getFreeze().toString());

            PromptBodyGeminiRequestModel prompt = buildPromptBody(basicPrompt);

            String geminiUrl = url + "?key=" + apiKey;

            GeminiResponseModel response = restTemplate.postForObject(geminiUrl, prompt, GeminiResponseModel.class);

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
                    .map(mp -> buildMealPrepResponse(mp, mealPrep.getRecipes()))
                    .collect(Collectors.toList());


            log.info("Generated {} meal preps from Gemini successfully", mealPreps.size());

            return mealPreps;
        } catch (JsonProcessingException e) {
            log.error("Failed to convert JSON in meal preps gemini adapter. ", e);
            throw new NotAvailableException(ErrorDescription.NOT_AVAILABLE.getValue());
        } catch (Exception e) {
            log.error("Error generating meal preps from Gemini", e);
            throw new NotAvailableException(ErrorDescription.NOT_AVAILABLE.getValue());
        }
    }

    private String buildRecipesJson(List<Recipe> recipes) throws JsonProcessingException {
        List<RecipeRequestGeminiModel> requests = recipes.stream().map(this::buildRecipeRequest).toList();
        return objectMapper.writeValueAsString(requests);
    }

    private RecipeRequestGeminiModel buildRecipeRequest(Recipe recipe) {
        return RecipeRequestGeminiModel.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .subtitle(recipe.getSubtitle())
                .description(recipe.getDescription())
                .preparationTime(recipe.getPreparationTime().getDescription())
                .cookLevelName(recipe.getCookLevel().getDescription())
                .dietName(recipe.getDiet().getDescription())
                .mealTypesNames(recipe.getMealTypes().stream().map(MealType::getDescription).toList())
                .allergiesNames(recipe.getAllergies().stream().map(Allergy::getDescription).toList())
                .dietaryNeedsNames(recipe.getDietaryNeeds().stream().map(DietaryNeed::getDescription).toList())
                .ingredientNames(recipe.getIngredients().stream().map(Ingredient::getName).toList())
                .build();
    }

    private String buildMealPrepsToNotInclude(List<MealPrep> mealPrepsToNotInclude) throws JsonProcessingException {
        List<String> notInclude = mealPrepsToNotInclude.stream().map(MealPrep::getTitle).toList();
        return objectMapper.writeValueAsString(notInclude);
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

    private MealPrep buildMealPrepResponse(MealPrepResponseGeminiModel mealPrepGeminiResponse, List<Recipe> recipes) {
        MealPrep mealPrepResponse = mealPrepGeminiResponse.toDomain();

        List<Long> recipeId = mealPrepGeminiResponse.getRecipeIds();

        List<Recipe> filteredRecipes = recipes.stream()
                .filter(recipe -> recipeId.contains(recipe.getId()))
                .toList();

        mealPrepResponse.setRecipes(filteredRecipes);

        return mealPrepResponse;
    }
}
