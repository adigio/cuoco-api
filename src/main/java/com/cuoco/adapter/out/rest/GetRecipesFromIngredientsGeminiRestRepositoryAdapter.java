package com.cuoco.adapter.out.rest;

import com.cuoco.adapter.out.rest.model.gemini.ContentGeminiRequestModel;
import com.cuoco.adapter.out.rest.model.gemini.GenerationConfigurationGeminiRequestModel;
import com.cuoco.adapter.out.rest.model.gemini.PartGeminiRequestModel;
import com.cuoco.adapter.out.rest.model.gemini.PromptBodyGeminiRequestModel;
import com.cuoco.application.port.out.GetRecipesFromIngredientsRepository;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.shared.FileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetRecipesFromIngredientsGeminiRestRepositoryAdapter implements GetRecipesFromIngredientsRepository {

    static final Logger log = LoggerFactory.getLogger(GetRecipesFromIngredientsGeminiRestRepositoryAdapter.class);

    private final String PROMPT = FileReader.execute("prompt/generateRecipe.txt");

    @Value("${gemini.api.url}")
    private String url;

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public GetRecipesFromIngredientsGeminiRestRepositoryAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String execute(List<Ingredient> ingredients) {

        log.info("Executing get recipes from gemini rest adapter with ingredients: {}", ingredients);

        PromptBodyGeminiRequestModel prompt = buildPromptBody(ingredients);

        String geminiUrl = url + "?key=" + apiKey;

        String response = restTemplate.postForObject(geminiUrl, prompt, String.class);

        log.info("Successfully generated recipes from Gemini");

        return response;
    }

    private PromptBodyGeminiRequestModel buildPromptBody(List<Ingredient> ingredients) {
        return new PromptBodyGeminiRequestModel(
                buildContentRequest(ingredients),
                new GenerationConfigurationGeminiRequestModel(
                    0.4
                )
        );
    }

    private List<ContentGeminiRequestModel> buildContentRequest(List<Ingredient> ingredients) {
        return List.of(
                new ContentGeminiRequestModel(buildPartsRequest(ingredients))
        );
    }

    private List<PartGeminiRequestModel> buildPartsRequest(List<Ingredient> ingredients) {

        String ingredientNames = ingredients.stream().map(Ingredient::getName).collect(Collectors.joining(","));

        return List.of(
                new PartGeminiRequestModel(
                        null,
                        PROMPT.formatted(ingredientNames)
                )
        );
    }
}
