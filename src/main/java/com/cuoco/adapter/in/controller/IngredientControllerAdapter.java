package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.ImageIngredientsResponse;
import com.cuoco.adapter.in.controller.model.IngredientResponse;
import com.cuoco.adapter.in.controller.model.TextRequest;
import com.cuoco.adapter.in.controller.model.UnitResponse;
import com.cuoco.application.port.in.GetIngredientsFromAudioCommand;
import com.cuoco.application.port.in.GetIngredientsFromTextCommand;
import com.cuoco.application.port.in.GetIngredientsGroupedFromImagesCommand;
import com.cuoco.application.usecase.model.Ingredient;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("/ingredients")
@Tag(name = "Ingredients", description = "Obtains ingredients from different sources")
public class IngredientControllerAdapter {

    private final GetIngredientsFromAudioCommand getIngredientsFromAudioCommand;
    private final GetIngredientsGroupedFromImagesCommand getIngredientsGroupedFromImagesCommand;
    private final GetIngredientsFromTextCommand getIngredientsFromTextCommand;

    public IngredientControllerAdapter(
            GetIngredientsFromAudioCommand getIngredientsFromAudioCommand,
            GetIngredientsGroupedFromImagesCommand getIngredientsGroupedFromImagesCommand,
            GetIngredientsFromTextCommand getIngredientsFromTextCommand
    ) {
        this.getIngredientsFromAudioCommand = getIngredientsFromAudioCommand;
        this.getIngredientsGroupedFromImagesCommand = getIngredientsGroupedFromImagesCommand;
        this.getIngredientsFromTextCommand = getIngredientsFromTextCommand;
    }

    @PostMapping("/audio")
    public ResponseEntity<List<IngredientResponse>> analyzeVoice(
            @RequestParam("audio") @NotNull MultipartFile audioFile,
            @RequestParam(value = "language", defaultValue = "es-ES") String language
    ) {
        log.info("Executing POST for audio processing to get ingredients with file {} (size: {} bytes)", audioFile.getOriginalFilename(), audioFile.getSize());

        List<Ingredient> ingredients = getIngredientsFromAudioCommand.execute(buildAudioCommand(audioFile, language));
        List<IngredientResponse> response = ingredients.stream().map(this::buildIngredientResponse).toList();

        log.info("Successfully extracted {} ingredients from voice", ingredients.size());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/image")
    public ResponseEntity<List<ImageIngredientsResponse>> getIngredients(@RequestParam("image") @NotNull List<MultipartFile> images) {
        log.info("Executing POST for image file processing to get ingredients, with {} images", images.size());

        Map<String, List<Ingredient>> ingredientsByImage = getIngredientsGroupedFromImagesCommand.execute(buildImageCommand(images));
        List<ImageIngredientsResponse> response = buildImageIngredientsResponseList(ingredientsByImage);

        log.info("Successfully extracted ingredients from {} images with separation", ingredientsByImage.size());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/text")
    public ResponseEntity<?> processText(@RequestBody @NotNull TextRequest request) {
        log.info("Processing text for ingredient extraction: {}", request);

        List<Ingredient> ingredients = getIngredientsFromTextCommand.execute(buildTextCommand(request));
        List<IngredientResponse> response = ingredients.stream().map(this::buildIngredientResponse).toList();

        log.info("Successfully extracted {} ingredients from text", ingredients.size());
        return ResponseEntity.ok(response);
    }

    private GetIngredientsFromAudioCommand.Command buildAudioCommand(MultipartFile audioFile, String language) {
        return GetIngredientsFromAudioCommand.Command.builder()
                .audioFile(audioFile)
                .language(language)
                .build();
    }

    private GetIngredientsGroupedFromImagesCommand.Command buildImageCommand(List<MultipartFile> images) {
        return GetIngredientsGroupedFromImagesCommand.Command.builder()
                .images(images)
                .build();
    }

    private GetIngredientsFromTextCommand.Command buildTextCommand(TextRequest request) {
        return GetIngredientsFromTextCommand.Command.builder()
                .text(request.getText())
                .source(request.getSource())
                .build();
    }

    private List<ImageIngredientsResponse> buildImageIngredientsResponseList(Map<String, List<Ingredient>> ingredientsByImage) {
        return ingredientsByImage.entrySet().stream()
                .map(imageIngredients -> ImageIngredientsResponse.builder()
                        .filename(imageIngredients.getKey())
                        .ingredients(imageIngredients.getValue().stream().map(this::buildIngredientResponse).toList())
                        .build())
                .toList();
    }

    private IngredientResponse buildIngredientResponse(Ingredient ingredient) {
        return IngredientResponse.builder()
                .name(ingredient.getName())
                .quantity(ingredient.getQuantity())
                .unit(UnitResponse.builder()
                        .id(ingredient.getUnit().getId())
                        .description(ingredient.getUnit().getDescription())
                        .symbol(ingredient.getUnit().getSymbol())
                        .build()
                )
                .confirmed(ingredient.getConfirmed())
                .source(ingredient.getSource())
                .build();
    }
}
