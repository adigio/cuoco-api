package com.cuoco.application.usecase.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserPreferences {
    private String cookLevel;

    private String diet;

    private List<DietaryNeeds> dietaryNeeds;

    private List<Allergies> allergies;
}
