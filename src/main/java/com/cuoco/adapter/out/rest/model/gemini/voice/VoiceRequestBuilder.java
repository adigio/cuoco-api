package com.cuoco.adapter.out.rest.model.gemini.voice;

import com.cuoco.adapter.out.rest.model.gemini.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VoiceRequestBuilder {

    /**
     * Construir request completo para Gemini con audio + prompt
     */
    public PromptBodyGeminiRequestModel buildVoiceRequest(String audioBase64, String format, String prompt) {
        return new PromptBodyGeminiRequestModel(
                buildContentRequest(audioBase64, format, prompt),
                new GenerationConfigurationGeminiRequestModel(0.2) // Temperature baja para consistencia
        );
    }

    /**
     * Construir contenido multimodal (audio + texto)
     */
    private List<ContentGeminiRequestModel> buildContentRequest(String audioBase64, String format, String prompt) {
        return List.of(
                new ContentGeminiRequestModel(buildPartsRequest(audioBase64, format, prompt))
        );
    }

    /**
     * Construir partes del request (prompt de texto + audio data)
     */
    private List<PartGeminiRequestModel> buildPartsRequest(String audioBase64, String format, String prompt) {
        String mimeType = AudioMimeTypeMapper.getMimeType(format);

        return List.of(
                // Parte 1: Prompt de texto con instrucciones
                new PartGeminiRequestModel(null, prompt),

                // Parte 2: Audio data en base64
                new PartGeminiRequestModel(
                        new InlineDataGeminiRequestModel(mimeType, audioBase64),
                        null
                )
        );
    }
}