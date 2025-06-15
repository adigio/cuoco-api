package com.cuoco.adapter.in.controller.model;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class RecipeFilterRequest {

    private String time;
    private String difficulty;
    private List<String> types;
    private String diet;
    private int quantity;
    private Integer people;
    private Boolean useProfilePreferences;

}