package com.cuoco.controller;

import com.cuoco.controller.dto.getRecipesDTO;
import com.cuoco.model.Ingrediente;
import com.cuoco.service.impl.GeminiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/recetas")
public class RecipeController {
    @Autowired
    private GeminiServiceImpl geminiService;

    public RecipeController(GeminiServiceImpl geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/generar")
    public ResponseEntity<?> generar(@RequestBody getRecipesDTO ingredientesDto) {
        if (ingredientesDto == null) {
            return ResponseEntity.badRequest().body("Error: no se proporcionaron ingredientes");
        }

        List ingredientes = Listaderecetas(ingredientesDto);
        if(ingredientes == null || ingredientes.size() == 0) {
            return ResponseEntity.badRequest().body("Error: no se proporcionaron ingredientes");
        }
        try {
            String receta = geminiService.generarRecetaDesdeIngredientes(ingredientes);
            return ResponseEntity.ok(receta);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al generar la receta: " + e.getMessage());
        }
    }

    private List Listaderecetas(getRecipesDTO ingredientesDto) {
        List ingredientes = new ArrayList<>();
        for (int i = 0; i < ingredientesDto.getIngredientes().size(); i++) {
            Ingrediente ingrediente = ingredientesDto.getIngredientes().get(i);
            ingredientes.add(ingrediente.getNombre());
        }
        return ingredientes;
    }


}
