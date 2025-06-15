package com.cuoco.application.usecase.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Recipe {
    private String id;
    private String name;
    private String preparationTime;
    private String image;
    private String subtitle;
    private String description;
    private List<Ingredient> ingredients;
    private String instructions;
}
