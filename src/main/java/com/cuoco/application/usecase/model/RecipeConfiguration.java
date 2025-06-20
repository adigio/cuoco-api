package com.cuoco.application.usecase.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RecipeConfiguration {

    private Integer recipesSize;
    private List<Long> notInclude;
}
