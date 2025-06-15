package com.cuoco.adapter.in.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String token;
    private ParametricResponse plan;
    private UserPreferencesResponse preferences;

    private List<ParametricResponse> dietaryNeeds;
    private List<ParametricResponse> allergies;
}
