package com.cuoco.adapter.out.rest.gemini.model;

import com.cuoco.application.usecase.model.Instruction;
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
public class InstructionResponseGeminiModel {
    private String title;
    private String time;
    private String description;

    public Instruction toDomain() {
        return Instruction.builder()
                .title(title)
                .time(time)
                .description(description)
                .build();
        }
    }

