package com.cuoco.factory.gemini;

import com.cuoco.adapter.out.rest.gemini.model.CookLevelResponseGeminiModel;
import com.cuoco.adapter.out.rest.gemini.model.IngredientResponseGeminiModel;
import com.cuoco.adapter.out.rest.gemini.model.InstructionResponseGeminiModel;
import com.cuoco.adapter.out.rest.gemini.model.MealPrepResponseGeminiModel;

import java.util.List;

public class MealPrepResponseGeminiModelFactory {
    public static MealPrepResponseGeminiModel create() {
        return MealPrepResponseGeminiModel.builder()
                .name("MealPrep ejemplo")
                .subtitle("Subtítulo ejemplo")
                .recipes(List.of("Receta 1", "Receta 2"))
                .instructions(List.of(
                        InstructionResponseGeminiModel.builder()
                                .title("Paso 1")
                                .description("Descripción del paso 1")
                                .build()
                ))
                .preparationTime("30 minutos")
                .ingredients(List.of(
                        IngredientResponseGeminiModel.builder()
                                .name("Pollo")
                                .quantity(200.0)
                                .unit("gramos")
                                .build()
                ))
                .cookLevel(CookLevelResponseGeminiModel.builder()
                        .description("Fácil")
                        .build())
                .build();
    }
}
