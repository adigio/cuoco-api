package com.cuoco.application.usecase.model;

import java.util.List;

public class RecipeFilter {

    private String time;
    private String difficulty;
    private List<String> types;
    private String diet;
    private int quantity;

    public RecipeFilter(String time, String difficulty, List<String> types, String diet, int quantity) {
        this.time = time;
        this.difficulty = difficulty;
        this.types = types;
        this.diet = diet;
        this.quantity = quantity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
