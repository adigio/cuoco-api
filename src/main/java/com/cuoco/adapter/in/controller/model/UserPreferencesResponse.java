package com.cuoco.adapter.in.controller.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPreferencesResponse {
    private CookLevelResponse cookLevel;
    private DietResponse diet;
}
