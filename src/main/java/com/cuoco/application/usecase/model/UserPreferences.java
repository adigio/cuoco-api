package com.cuoco.application.usecase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserPreferences {
    private Long id;
    private CookLevel cookLevel;
    private Diet diet;
}
