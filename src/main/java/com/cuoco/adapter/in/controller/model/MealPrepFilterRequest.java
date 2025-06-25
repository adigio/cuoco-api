package com.cuoco.adapter.in.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MealPrepFilterRequest {
    private Boolean freeze;
    private Integer preparationTimeId;
    private Integer servings;
    private Integer cookLevelId;
    private List<Integer> typeIds;
    private Integer dietId;
    private List<Integer> allergiesIds;
    private List<Integer> dietaryNeedsIds;
}
