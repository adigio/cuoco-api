package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.helper.AudioFileProcessor;
import com.cuoco.adapter.in.controller.model.IngredientsResponse;
import com.cuoco.adapter.in.controller.model.IngredientsResponseMapper;
import com.cuoco.application.port.in.GetIngredientsFromVoiceCommand;
import com.cuoco.application.usecase.model.Ingredient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/analyze-voice")
public class VoiceControllerAdapter {

    private final GetIngredientsFromVoiceCommand getIngredientsFromVoiceCommand;
    private final IngredientsResponseMapper ingredientsResponseMapper;
    private final AudioFileProcessor audioFileProcessor;

    public VoiceControllerAdapter(GetIngredientsFromVoiceCommand getIngredientsFromVoiceCommand,
                                  IngredientsResponseMapper ingredientsResponseMapper,
                                  AudioFileProcessor audioFileProcessor) {
        this.getIngredientsFromVoiceCommand = getIngredientsFromVoiceCommand;
        this.ingredientsResponseMapper = ingredientsResponseMapper;
        this.audioFileProcessor = audioFileProcessor;
    }

    @PostMapping("/")
    public ResponseEntity<?> analyzeVoice(
            @RequestParam("audio") MultipartFile audioFile,
            @RequestParam(value = "language", defaultValue = "es-ES") String language) {

        try {
            log.info("Processing voice file: {} (size: {} bytes)",
                    audioFile.getOriginalFilename(), audioFile.getSize());

            if (!audioFileProcessor.isValidAudioFile(audioFile)) {
                return ResponseEntity.badRequest().body(audioFileProcessor.getSupportedFormatsMessage());
            }

            String audioBase64 = audioFileProcessor.convertToBase64(audioFile);
            String format = audioFileProcessor.getAudioFormat(audioFile);

            GetIngredientsFromVoiceCommand.Command command =
                    new GetIngredientsFromVoiceCommand.Command(audioBase64, format, language);

            List<Ingredient> ingredients = getIngredientsFromVoiceCommand.execute(command);
            IngredientsResponse response = ingredientsResponseMapper.toResponse(ingredients);

            log.info("Successfully extracted {} ingredients from voice", ingredients.size());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error processing voice: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body("Error al procesar el audio: " + e.getMessage());
        }
    }
}