package com.cuoco.application.usecase.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RecipeConfiguration {

    private Integer size;
    private List<Recipe> notInclude;

    private ParametricData parametricData;
}
