package com.cuoco.adapter.out.rest.gemini.model;

import com.cuoco.application.usecase.model.Step;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StepResponseGeminiModel {
    private String title;
    private Integer number;
    private String description;
    private String time;

    public Step toDomain() {
        return Step.builder()
                .title(title)
                .number(number)
                .description(description)
                .time(time)
                .build();
    }
}
