package com.cuoco.adapter.in.controller.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CookLevelResponse {
    private Integer id;
    private String description;
}
