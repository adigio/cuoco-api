package com.cuoco.application.usecase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class UserPreferences {
    private CookLevel cookLevel;
    private Diet diet;
}
