package com.cuoco.application.usecase.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class Calendar {
    private Day day;
    private LocalDate date;
    private List<CalendarRecipe> recipes;
}
