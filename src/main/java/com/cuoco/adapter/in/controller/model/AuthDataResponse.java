package com.cuoco.adapter.in.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthDataResponse {
    private UserResponse user;
}
