package com.cuoco.adapter.out.rest.model.gemini;

import com.cuoco.adapter.out.rest.model.gemini.voice.VoiceRequestBuilder;
import com.cuoco.adapter.out.rest.model.gemini.voice.VoiceResponseParser;
import com.cuoco.adapter.out.rest.model.gemini.PromptBodyGeminiRequestModel;
import com.cuoco.application.port.out.GetIngredientsFromVoiceRepository;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.shared.FileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class GeminiVoiceRepositoryAdapter implements GetIngredientsFromVoiceRepository {

    private static final Logger log = LoggerFactory.getLogger(GeminiVoiceRepositoryAdapter.class);

    private final String VOICE_PROMPT = FileReader.execute("prompt/generateIngredientsFromVoice.txt");

    @Value("${gemini.api.url}")
    private String url;

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final VoiceRequestBuilder voiceRequestBuilder;
    private final VoiceResponseParser voiceResponseParser;

    public GeminiVoiceRepositoryAdapter(RestTemplate restTemplate,
                                        VoiceRequestBuilder voiceRequestBuilder,
                                        VoiceResponseParser voiceResponseParser) {
        this.restTemplate = restTemplate;
        this.voiceRequestBuilder = voiceRequestBuilder;
        this.voiceResponseParser = voiceResponseParser;
    }

    @Override
    public List<Ingredient> processVoice(String audioBase64, String format, String language) {
        log.info("🎙️ Processing voice with Gemini - format: {}, language: {}", format, language);

        try {
            // 1. Construir request usando builder modular
            PromptBodyGeminiRequestModel request = voiceRequestBuilder.buildVoiceRequest(audioBase64, format, VOICE_PROMPT);
            String geminiUrl = url + "?key=" + apiKey;

            // 2. HTTP call a Gemini API
            String response = restTemplate.postForObject(geminiUrl, request, String.class);

            // 3. Parsear respuesta usando parser modular
            List<Ingredient> ingredients = voiceResponseParser.parseIngredientsFromResponse(response);

            log.info("✅ Successfully extracted {} ingredients from voice", ingredients.size());
            return ingredients;

        } catch (Exception e) {
            log.error("❌ Error processing voice with Gemini: {}", e.getMessage(), e);
            throw new RuntimeException("Error processing voice: " + e.getMessage(), e);
        }
    }

    @Override
    @Async
    public CompletableFuture<List<Ingredient>> processVoiceAsync(String audioBase64, String format, String language) {
        log.info("🎙️ Processing voice ASYNC with Gemini - format: {}, language: {}", format, language);

        return CompletableFuture.supplyAsync(() -> {
            try {
                return processVoice(audioBase64, format, language);
            } catch (Exception e) {
                log.error("❌ Async error processing voice: {}", e.getMessage(), e);
                throw new RuntimeException("Async error processing voice: " + e.getMessage(), e);
            }
        });
    }
} 