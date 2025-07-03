package com.cuoco.application.usecase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Step {
    private Long id;
    private String title;
    private Integer number;
    private String description;
    private String time;
    private String imageName;
    private byte[] imageData;
} 