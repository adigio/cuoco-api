package com.cuoco.adapter.in.controller.model;

import lombok.Data;

import java.util.List;

@Data
public class AuthRequest {
    private String name;
    private String password;
    private String email;
    private String plan;
    private String cookLevel;
    private String diet;
    private List<String> dietaryNeeds;
    private List<String> allergies;
}
