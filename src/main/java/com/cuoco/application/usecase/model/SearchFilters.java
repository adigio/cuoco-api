package com.cuoco.application.usecase.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchFilters {
    private Integer size;
    private Boolean random;
}
