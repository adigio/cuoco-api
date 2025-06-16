package com.cuoco.adapter.out.rest.gemini;

import com.cuoco.adapter.exception.NotAvailableException;
import com.cuoco.adapter.out.rest.gemini.model.IngredientResponseGeminiModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.ContentGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.GeminiResponseModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.GenerationConfigurationGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.InlineDataGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.PartGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.PromptBodyGeminiRequestModel;
import com.cuoco.adapter.out.rest.model.gemini.GeminiResponseMapper;
import com.cuoco.adapter.utils.Utils;
import com.cuoco.application.port.out.GetIngredientsFromImageRepository;
import com.cuoco.application.port.out.GetIngredientsFromImagesGroupedRepository;
import com.cuoco.application.usecase.model.File;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.shared.FileReader;
import com.cuoco.shared.model.ErrorDescription;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class GetIngredientsFromImageGroupedGeminiRestRepositoryAdapter implements GetIngredientsFromImagesGroupedRepository {

    private final static String SOURCE = "image";
    private final String PROMPT = FileReader.execute("prompt/recognizeIngredientsFromImage.txt");

    @Value("${gemini.api.url}")
    private String url;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.temperature}")
    private Double temperature;

    private final RestTemplate restTemplate;
    private final GeminiResponseMapper geminiResponseMapper;

    public GetIngredientsFromImageGroupedGeminiRestRepositoryAdapter(
            RestTemplate restTemplate,
            GeminiResponseMapper geminiResponseMapper
    ) {
        this.restTemplate = restTemplate;
        this.geminiResponseMapper = geminiResponseMapper;
    }

    @Override
    public Map<String, List<Ingredient>> execute(List<File> files) {
        log.info("Getting ingredients from Gemini grouped by image");

        Map<String, List<Ingredient>> ingredientsByImage = new LinkedHashMap<>();

        try {
            for (File file : files) {
                PromptBodyGeminiRequestModel prompt = buildPromptBody(file.getFileBase64(), file.getMimeType(), PROMPT);
                String geminiUrl = url + "?key=" + apiKey;
                GeminiResponseModel response = restTemplate.postForObject(geminiUrl, prompt, GeminiResponseModel.class);

                String recipeResponseText = Utils.sanitizeJsonResponse(response);

                ObjectMapper mapper = new ObjectMapper();

                List<IngredientResponseGeminiModel> ingredientsResponse = mapper.readValue(
                        recipeResponseText,
                        new TypeReference<>() {}
                );

                List<Ingredient> ingredientsFromImage = ingredientsResponse.stream().map(IngredientResponseGeminiModel::toDomain).toList();

                ingredientsFromImage.forEach(ingredient -> ingredient.setSource(SOURCE));

                log.info("Extracted {} ingredients from image {}", ingredientsFromImage.size(), file.getFileName());

                ingredientsByImage.put(file.getFileName(), ingredientsFromImage);
            }



            log.info("Successfully got all ingredients grouped by image processed by Gemini");

            return ingredientsByImage;
        } catch (Exception e) {
            log.error("Error sending images to Gemini to process ingredients: ", e);
            throw new NotAvailableException(ErrorDescription.NOT_AVAILABLE.getValue());
        }
    }

    private PromptBodyGeminiRequestModel buildPromptBody(String imageBase64, String mimeType, String prompt) {
        return PromptBodyGeminiRequestModel.builder()
                .contents(List.of(ContentGeminiRequestModel.builder().parts(buildPartsRequest(imageBase64, mimeType, prompt)).build()))
                .generationConfig(GenerationConfigurationGeminiRequestModel.builder().temperature(temperature).build())
                .build();
    }

    private List<PartGeminiRequestModel> buildPartsRequest(String imageBase64, String mimeType, String prompt) {
            return List.of(
                    PartGeminiRequestModel.builder()
                            .text(prompt)
                            .build(),
                    PartGeminiRequestModel.builder()
                            .inlineData(buildInlineData(imageBase64, mimeType))
                            .build()
            );
    }

    private InlineDataGeminiRequestModel buildInlineData(String imageBase64, String mimeType) {
        return InlineDataGeminiRequestModel.builder()
                .mimeType(mimeType)
                .data(imageBase64)
                .build();
    }
}