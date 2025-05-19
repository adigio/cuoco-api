package com.cuoco.presentation.controller;

import com.cuoco.domain.usecase.GetRecipesFromIngredientsUsecase;
import com.cuoco.presentation.controller.model.IngredientDTO;
import com.cuoco.presentation.controller.model.RecipeRequest;
import com.cuoco.infrastructure.repository.hibernate.model.IngredientHibernateModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {


    private final GetRecipesFromIngredientsUsecase getRecipesFromIngredientsUsecase;

    public RecipeController(GetRecipesFromIngredientsUsecase getRecipesFromIngredientsUsecase) {
        this.getRecipesFromIngredientsUsecase = getRecipesFromIngredientsUsecase;
    }

    @PostMapping()
    public ResponseEntity<?> generar(@RequestBody List<IngredientDTO> ingredients) {


        List ingredientes = Listaderecetas(ingredientesDto);

        if(ingredientes == null || ingredientes.size() == 0) {
            return ResponseEntity.badRequest().body("Error: no se proporcionaron ingredientes");
        }

        try {
            String receta = getRecipesFromIngredientsUsecase.execute(ingredientes);

            return ResponseEntity.ok(receta);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al generar la receta: " + e.getMessage());
        }
    }

    private List Listaderecetas(RecipeRequest ingredientesDto) {
        List ingredientes = new ArrayList<>();
        for (int i = 0; i < ingredientesDto.getIngredientes().size(); i++) {
            IngredientHibernateModel ingrediente = ingredientesDto.getIngredientes().get(i);
            ingredientes.add(ingrediente.getNombre());
        }
        return ingredientes;
    }


}
