package com.cuoco.adapter.in.controller;

import com.cuoco.application.port.in.GetMealPrepFromIngredientsCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/meal-preps")
public class MealPrepControllerAdapter {

    private final GetMealPrepFromIngredientsCommand getMealPrepFromIngredientsCommand;

    public MealPrepControllerAdapter(GetMealPrepFromIngredientsCommand getMealPrepFromIngredientsCommand) {
        this.getMealPrepFromIngredientsCommand = getMealPrepFromIngredientsCommand;
    }

    @PostMapping
    public ResponseEntity<List<MealPrepResponse>> generate(@RequestBody MealPrepRequest mealPrepRequest){

        log.info("Executing GET mealPrep from ingredients with body {}", mealPrepRequest);

        List<MealPrep> mealPreps = getMealPrepFromIngredientsCommand.execute(mealPrepRequest);

        List<MealPrepResponse> mealPrepsResponse = mealPreps.stream().map(mealPrepsResponse).toList();

        log.info("Successfully generated recipes");
        return ResponseEntity.ok(mealPrepsResponse);
    }
}
