package com.cuoco.adapter.in.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRecipeCalendarResponse {
    private Long recipeId;
    private String title;
    private String img;
    private int mealType;

    public UserRecipeCalendarResponse(Long idReceta, String title, String img, int mealType) {
        this.recipeId = idReceta;
        this.title = title;
        this.img = img;
        this.mealType = mealType;
    }

    public Long getIdReceta() {
        return recipeId;
    }

    public void setIdReceta(Long idReceta) {
        this.recipeId = idReceta;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getMealType() {
        return mealType;
    }

    public void setMealType(int mealType) {
        this.mealType = mealType;
    }
}
