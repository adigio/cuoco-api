package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.*;
import com.cuoco.application.port.in.GetUserRecipeCalendarCommand;
import com.cuoco.application.port.in.SaveUserRecipeCalendarCommand;
import com.cuoco.application.port.in.SaveUserRecipeCommand;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.UserRecipe;
import com.cuoco.application.usecase.model.UserRecipeCalendar;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/recipes/calendar")
public class UserRecipeCalendarControllerAdapter {
    static final Logger log = LoggerFactory.getLogger(UserRecipeCalendarControllerAdapter.class);

    private SaveUserRecipeCalendarCommand saveUserRecipeCalendarCommand;
    private GetUserRecipeCalendarCommand getUserRecipeCalendarCommand;

    public UserRecipeCalendarControllerAdapter(SaveUserRecipeCalendarCommand saveUserRecipeCalendarCommand, GetUserRecipeCalendarCommand getUserRecipeCalendarCommand) {
        this.saveUserRecipeCalendarCommand = saveUserRecipeCalendarCommand;
        this.getUserRecipeCalendarCommand = getUserRecipeCalendarCommand;
    }

    @PostMapping("/")
    public ResponseEntity<Boolean> save(@RequestBody @Valid List<SaveCalendarRequest> requests) {

        log.info("Trying to save these recipes in user calendar");
        Boolean response = saveUserRecipeCalendarCommand.execute(buildingThisCaseCalendar(requests));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<?> getFavourites() {
        List<UserRecipeCalendar> calendarRecipes = getUserRecipeCalendarCommand.execute();
        List<UserRecipeCalendarResponse> response = buildResponse(calendarRecipes);
        return ResponseEntity.ok(response);
    }

    private SaveUserRecipeCalendarCommand.Command buildingThisCaseCalendar(List<SaveCalendarRequest> requests) {
        List<SaveUserRecipeCalendarCommand.Command.CalendarCommand> CalendarCommands = requests.stream()
                .map(req -> new SaveUserRecipeCalendarCommand.Command.CalendarCommand(
                        req.getDayId(),
                        req.getRecipeId(),
                        req.getMealtypeId()
                ))
                .toList();
        return new SaveUserRecipeCalendarCommand.Command(CalendarCommands);
    }

    private List<UserRecipeCalendarResponse> buildResponse(List<UserRecipeCalendar> calendarRecipes) {
        List<UserRecipeCalendar> filtrados = calendarRecipes.stream()
                .filter(urc -> {
                    LocalDate fecha = urc.getDate();
                    LocalDate hoy = LocalDate.now();
                    LocalDate dentroDe7Dias = hoy.plusDays(7);
                    return (fecha != null && !fecha.isBefore(hoy) && !fecha.isAfter(dentroDe7Dias));
                })
                .collect(Collectors.toList());

        List<UserRecipeCalendarResponse> responses = filtrados.stream()
                .map(urc -> UserRecipeCalendarResponse.builder()
                        .idReceta(urc.getRecipe().getId())
                        .title(urc.getRecipe().getName())
                        .img(urc.getRecipe().getImage())
                        .mealType(urc.getMealType().getId())
                        .build()
                )
                .collect(Collectors.toList());

        return responses;
    }
}
