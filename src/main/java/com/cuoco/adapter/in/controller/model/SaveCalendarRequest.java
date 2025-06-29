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
public class SaveCalendarRequest {

    private int dayId;
    private Long recipeId;
    private int mealtypeId;

    public SaveCalendarRequest(int dayId, Long recipeId, int mealtypeId) {
        this.dayId = dayId;
        this.recipeId = recipeId;
        this.mealtypeId = mealtypeId;
    }

    public int getDayId() {
        return dayId;
    }

    public void setDayId(int dayId) {
        this.dayId = dayId;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public int getMealtypeId() {
        return mealtypeId;
    }

    public void setMealtypeId(int mealtypeId) {
        this.mealtypeId = mealtypeId;
    }

    @Override
    public String toString() {
        return "SaveCalendarRequest{" +
                "dayId=" + dayId +
                ", recipeId=" + recipeId +
                ", mealtypeId=" + mealtypeId +
                '}';
    }
}
