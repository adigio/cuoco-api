package com.cuoco.adapter.out.rest.gemini;

import com.cuoco.adapter.exception.UnprocessableException;
import com.cuoco.adapter.out.rest.gemini.model.IngredientResponseGeminiModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.ContentGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.GeminiResponseModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.GenerationConfigurationGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.InlineDataGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.PartGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.PromptBodyGeminiRequestModel;
import com.cuoco.adapter.utils.Utils;
import com.cuoco.application.port.out.GetIngredientsFromAudioRepository;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.shared.FileReader;
import com.cuoco.shared.utils.FileUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
public class GetIngredientsFromAudioGeminiRestRepositoryAdapter implements GetIngredientsFromAudioRepository {

    private final String VOICE_PROMPT = FileReader.execute("prompt/recognizeIngredientsFromAudioPrompt.txt");

    @Value("${gemini.api.url}")
    private String url;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.temperature}")
    private Double temperature;

    private final RestTemplate restTemplate;

    public GetIngredientsFromAudioGeminiRestRepositoryAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Ingredient> execute(String audioBase64, String format, String language) {
        log.info("Executing voice processing with Gemini with format {} and language {}", format, language);

        try {
            PromptBodyGeminiRequestModel request = buildPromptBody(audioBase64, format, VOICE_PROMPT);

            String geminiUrl = url + "?key=" + apiKey;

            GeminiResponseModel response = restTemplate.postForObject(geminiUrl, request, GeminiResponseModel.class);

            String recipeResponseText = Utils.sanitizeJsonResponse(response);

            ObjectMapper mapper = new ObjectMapper();

            List<IngredientResponseGeminiModel> ingredientsResponse = mapper.readValue(
                    recipeResponseText,
                    new TypeReference<>() {}
            );

            List<Ingredient> ingredients = ingredientsResponse.stream().map(IngredientResponseGeminiModel::toDomain).toList();

            log.info("Successfully extracted {} ingredients from audio", ingredients.size());

            return ingredients;
        } catch (Exception e) {
            log.error("Error processing voice with Gemini: {}", e.getMessage(), e);
            throw new UnprocessableException("Error processing voice: " + e.getMessage());
        }
    }

    private PromptBodyGeminiRequestModel buildPromptBody(String audioBase64, String format, String prompt) {
        return PromptBodyGeminiRequestModel.builder()
                .contents(List.of(ContentGeminiRequestModel.builder().parts(buildPartsRequest(audioBase64, format, prompt)).build()))
                .generationConfig(GenerationConfigurationGeminiRequestModel.builder().temperature(temperature).build())
                .build();
    }

    private List<PartGeminiRequestModel> buildPartsRequest(String audioBase64, String format, String prompt) {
        return List.of(
                PartGeminiRequestModel.builder()
                        .text(prompt)
                        .build(),
                PartGeminiRequestModel.builder()
                        .inlineData(buildInlineData(audioBase64, format))
                        .build()
        );
    }

    private InlineDataGeminiRequestModel buildInlineData(String audioBase64, String format) {
        String mimeType = FileUtils.getAudioMimeType(format);

        return InlineDataGeminiRequestModel.builder()
                .mimeType(mimeType)
                .data(audioBase64)
                .build();
    }
} 