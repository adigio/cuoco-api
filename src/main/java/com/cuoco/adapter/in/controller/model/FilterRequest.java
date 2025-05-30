package com.cuoco.adapter.in.controller.model;

import java.util.List;

public class FilterRequest {

    private String time;
    private String difficulty;
    private List<String> types;
    private String diet;
    private int quantity;

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

    @Override
    public String toString() {
        return "FilterRequest{" +
                "time='" + time + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", types=" + types +
                ", diet='" + diet + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
