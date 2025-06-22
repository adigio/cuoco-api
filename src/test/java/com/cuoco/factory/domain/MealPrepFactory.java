package com.cuoco.factory.domain;

import com.cuoco.adapter.in.controller.model.IngredientRequest;
import com.cuoco.adapter.in.controller.model.MealPrepRequest;
import com.cuoco.application.usecase.model.*;
import com.cuoco.adapter.in.controller.model.MealPrepFilterRequest;

import java.util.List;

public class MealPrepFactory {
    public static MealPrep create() {
        return MealPrep.builder()
                .id(1L)
                .name("Meal Prep Semanal")
                .subtitle("Ideal para almuerzos y cenas de la semana")
                .recipes(List.of("Pollo al horno", "Arroz con verduras", "Tarta de espinaca"))
                .preparationTime("2 h")
                .instructions(List.of(
                        Instruction.builder()
                                .title("Preparar ingredientes")
                                .description("Lavar y cortar todos los vegetales. Precalentar el horno.")
                                .time("30 min")
                                .build(),
                        Instruction.builder()
                                .title("Cocinar")
                                .description("Hornear el pollo, hervir arroz, preparar la tarta.")
                                .time("1 h 30 min")
                                .build()
                ))
                .ingredients(List.of(
                        Ingredient.builder()
                                .name("pollo")
                                .quantity(1.0)
                                .unit(Unit.builder().id(1).description("Kilogramo").symbol("kg").build())
                                .build(),
                        Ingredient.builder()
                                .name("arroz")
                                .quantity(200.0)
                                .unit(Unit.builder().id(2).description("Gramo").symbol("gr").build())
                                .build()
                ))
                .cookLevel(CookLevel.builder().id(1).description("Bajo").build())
                .build();
    }

    public static MealPrepRequest getMealPrepRequest() {
        return MealPrepRequest.builder()
                .ingredients(List.of(
                        IngredientRequest.builder()
                                .name("pollo")
                                .source("user")
                                .confirmed(true)
                                .build(),
                        IngredientRequest.builder()
                                .name("arroz")
                                .source("user")
                                .confirmed(true)
                                .build()
                ))
                .filters(
                        MealPrepFilterRequest.builder()
                                .difficulty("Bajo")
                                .diet("sin gluten")
                                .quantity(3)
                                .freeze(true)
                                .types(List.of("almuerzo", "cena"))
                                .build()
                )
                .build();
    }
}
