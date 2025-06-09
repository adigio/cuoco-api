package com.cuoco.adapter.in.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class RecipeRequest {

    private List<IngredientRequest> ingredients;
    private FilterRequest filters;

    @Override
    public String toString() {
        return "RecipeRequest{" +
                "ingredients=" + ingredients +
                ", filters=" + filters +
                '}';
    }
}