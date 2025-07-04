package com.cuoco.application.usecase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class User {

    private Long id;
    private String name;
    private String email;
    private String password;
    private Plan plan;
    private Boolean active;
    private UserPreferences preferences;
    private LocalDateTime createdAt;

    private List<DietaryNeed> dietaryNeeds;
    private List<Allergy> allergies;
}


