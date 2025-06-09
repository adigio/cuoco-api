package com.cuoco.adapter.in.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private PlanResponse plan;
    private UserPreferencesResponse preferences;

    private List<String> dietaryNeeds;
    private List<String> allergies;
}
