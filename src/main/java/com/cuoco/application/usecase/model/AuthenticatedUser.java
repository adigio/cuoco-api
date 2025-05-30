package com.cuoco.application.usecase.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AuthenticatedUser {

    private User user;
    private String token;
    private List<String> roles;

}
