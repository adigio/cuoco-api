package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.TextRequest;
import com.cuoco.adapter.in.controller.model.IngredientsResponse;
import com.cuoco.adapter.in.controller.model.IngredientsResponseMapper;
import com.cuoco.application.port.in.GetIngredientsFromTextCommand;
import com.cuoco.application.usecase.model.Ingredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analyze-text")
public class TextControllerAdapter {

    private static final Logger log = LoggerFactory.getLogger(TextControllerAdapter.class);

    private final GetIngredientsFromTextCommand getIngredientsFromTextCommand;
    private final IngredientsResponseMapper ingredientsResponseMapper;

    public TextControllerAdapter(GetIngredientsFromTextCommand getIngredientsFromTextCommand,
                                 IngredientsResponseMapper ingredientsResponseMapper) {
        this.getIngredientsFromTextCommand = getIngredientsFromTextCommand;
        this.ingredientsResponseMapper = ingredientsResponseMapper;
    }

    @PostMapping("/")
    public ResponseEntity<?> processText(@RequestBody TextRequest request) {
        try {
            log.info("Processing text for ingredient extraction: {}", request);

            List<Ingredient> ingredients = getIngredientsFromTextCommand.execute(buildTextCommand(request));

            IngredientsResponse response = ingredientsResponseMapper.toResponse(ingredients);

            log.info("Successfully extracted {} ingredients from text", ingredients.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error processing text: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error al procesar el texto: " + e.getMessage());
        }
    }

    private GetIngredientsFromTextCommand.Command buildTextCommand(TextRequest request) {
        return new GetIngredientsFromTextCommand.Command(request.getText(), request.getSource());
    }
}