package com.cuoco.adapter.in.controller.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Email is required")
    private String email;
}
