package com.cuoco.adapter.out.rest.gemini;

import com.cuoco.adapter.exception.UnprocessableException;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.ContentGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.GenerationConfigurationGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.InlineDataGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.PartGeminiRequestModel;
import com.cuoco.adapter.out.rest.model.gemini.voice.AudioMimeTypeMapper;
import com.cuoco.adapter.out.rest.model.gemini.voice.VoiceResponseParser;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.PromptBodyGeminiRequestModel;
import com.cuoco.application.port.out.GetIngredientsFromAudioRepository;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.shared.FileReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class GetIngredientsFromAudioGeminiRestRepositryAdapter implements GetIngredientsFromAudioRepository {

    private final String VOICE_PROMPT = FileReader.execute("prompt/recognizeIngredientsFromVoice.txt");

    @Value("${gemini.api.url}")
    private String url;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.temperature}")
    private Double temperature;

    private final RestTemplate restTemplate;
    private final VoiceResponseParser voiceResponseParser;

    public GetIngredientsFromAudioGeminiRestRepositryAdapter(
            RestTemplate restTemplate,
            VoiceResponseParser voiceResponseParser
    ) {
        this.restTemplate = restTemplate;
        this.voiceResponseParser = voiceResponseParser;
    }

    @Override
    public List<Ingredient> processVoice(String audioBase64, String format, String language) {
        log.info("Executing voice processing with Gemini with format {} and language {}", format, language);

        try {
            PromptBodyGeminiRequestModel request = buildPromptBody(audioBase64, format, VOICE_PROMPT);

            String geminiUrl = url + "?key=" + apiKey;

            String response = restTemplate.postForObject(geminiUrl, request, String.class);

            List<Ingredient> ingredients = voiceResponseParser.parseIngredientsFromResponse(response);

            log.info("Successfully extracted {} ingredients from voice", ingredients.size());

            return ingredients;
        } catch (Exception e) {
            log.error("Error processing voice with Gemini: {}", e.getMessage(), e);
            throw new UnprocessableException("Error processing voice: " + e.getMessage());
        }
    }

    @Override
    @Async
    public CompletableFuture<List<Ingredient>> processVoiceAsync(String audioBase64, String format, String language) {
        log.info("Executing asynchronous voice processing in Gemini with format {} and language {}", format, language);

        return CompletableFuture.supplyAsync(() -> {
            try {
                return processVoice(audioBase64, format, language);
            } catch (Exception e) {
                log.error("Async error processing voice: {}", e.getMessage(), e);
                throw new RuntimeException("Async error processing voice: " + e.getMessage(), e);
            }
        });
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
        String mimeType = AudioMimeTypeMapper.getMimeType(format);

        return InlineDataGeminiRequestModel.builder()
                .mimeType(mimeType)
                .data(audioBase64)
                .build();
    }
} 