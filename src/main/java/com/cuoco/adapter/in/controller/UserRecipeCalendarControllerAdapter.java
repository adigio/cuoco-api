package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.*;
import com.cuoco.application.port.in.GetUserRecipeCalendarCommand;
import com.cuoco.application.port.in.SaveUserRecipeCalendarCommand;
import com.cuoco.application.usecase.model.UserRecipeCalendar;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
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
        Map<String, List<UserRecipeCalendarResponse>> response = buildResponseMapAndOrder(calendarRecipes);
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

    private Map<String, List<UserRecipeCalendarResponse>> buildResponseMapAndOrder(List<UserRecipeCalendar> calendarRecipes) {

        DayOfWeek today = LocalDate.now().getDayOfWeek();
        Locale locale = Locale.getDefault();

        Map<DayOfWeek, List<UserRecipeCalendarResponse>> groupedByDayOfWeek = calendarRecipes.stream()
                .collect(Collectors.groupingBy(
                        urc -> urc.getDate().getDayOfWeek(),
                        Collectors.mapping(urc -> UserRecipeCalendarResponse.builder()
                                .recipeId(urc.getRecipe().getId())
                                .title(urc.getRecipe().getName())
                                .img(urc.getRecipe().getImage())
                                .mealType(urc.getMealType().getId())
                                .build(), Collectors.toList())
                ));

        return Arrays.stream(DayOfWeek.values())
                .sorted(Comparator.comparingInt(d -> (d.getValue() - today.getValue() + 7) % 7))
                .collect(Collectors.toMap(
                        d -> d.getDisplayName(TextStyle.FULL, locale),
                        d -> groupedByDayOfWeek.getOrDefault(d, List.of()),
                        (a, b) -> b,
                        LinkedHashMap::new
                ));
    }
}
