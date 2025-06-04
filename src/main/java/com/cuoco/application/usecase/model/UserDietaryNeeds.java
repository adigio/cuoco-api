package com.cuoco.application.usecase.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDietaryNeeds {
    private Long userId;

    private Long dietaryNeedId;

}
