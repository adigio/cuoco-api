package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.IngredientsResponseMapper;
import com.cuoco.adapter.in.controller.model.IngredientsResponse;
import com.cuoco.application.port.in.GetIngredientsFromFileCommand;
import com.cuoco.application.usecase.model.Ingredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analyze-images")
public class UploadControllerAdapter {

    private static final Logger log = LoggerFactory.getLogger(UploadControllerAdapter.class);

    private final GetIngredientsFromFileCommand getIngredientsFromFileCommand;
    private final IngredientsResponseMapper ingredientsResponseMapper;

    public UploadControllerAdapter(GetIngredientsFromFileCommand getIngredientsFromFileCommand,
                                   IngredientsResponseMapper ingredientsResponseMapper) {
        this.getIngredientsFromFileCommand = getIngredientsFromFileCommand;
        this.ingredientsResponseMapper = ingredientsResponseMapper;
    }

    @PostMapping("/")
    public ResponseEntity<?> getIngredients(@RequestParam("files") List<MultipartFile> files) {
        try {
            log.info("Processing {} image files for ingredient analysis", files.size());

            Map<String, List<Ingredient>> ingredientsByImage = getIngredientsFromFileCommand.executeWithSeparation(buildIngredientsFromFileCommand(files));

            IngredientsResponse ingredientsResponse = ingredientsResponseMapper.toImageSeparateResponse(ingredientsByImage);

            log.info("Successfully extracted ingredients from {} images with separation", ingredientsByImage.size());
            return ResponseEntity.ok(ingredientsResponse);
        } catch (Exception e) {
            log.error("Error processing images: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error al procesar la imagen: " + e.getMessage());
        }
    }

    private GetIngredientsFromFileCommand.Command buildIngredientsFromFileCommand(List<MultipartFile> files) {
        return new GetIngredientsFromFileCommand.Command(files);
    }
}