package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.CalendarRecipeResponse;
import com.cuoco.adapter.in.controller.model.CalendarResponse;
import com.cuoco.adapter.in.controller.model.DayResponse;
import com.cuoco.adapter.in.controller.model.ParametricResponse;
import com.cuoco.adapter.in.controller.model.RecipeCalendarRequest;
import com.cuoco.adapter.in.controller.model.RecipeResponse;
import com.cuoco.adapter.in.controller.model.UserRecipeCalendarRequest;
import com.cuoco.application.port.in.CreateUserRecipeCalendarCommand;
import com.cuoco.application.port.in.GetUserCalendarQuery;
import com.cuoco.application.usecase.model.Calendar;
import com.cuoco.application.usecase.model.CalendarRecipe;
import com.cuoco.application.usecase.model.Recipe;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/calendar")
public class UserCalendarControllerAdapter {

    private final CreateUserRecipeCalendarCommand createUserRecipeCalendarCommand;
    private final GetUserCalendarQuery getUserCalendarQuery;

    public UserCalendarControllerAdapter(CreateUserRecipeCalendarCommand createUserRecipeCalendarCommand, GetUserCalendarQuery getUserCalendarQuery) {
        this.createUserRecipeCalendarCommand = createUserRecipeCalendarCommand;
        this.getUserCalendarQuery = getUserCalendarQuery;
    }

    @PostMapping()
    public ResponseEntity<?> save(@RequestBody @Valid List<UserRecipeCalendarRequest> requests) {
        log.info("Executing POST for user recipe calendar creation");

        createUserRecipeCalendarCommand.execute(buildCommand(requests));

        log.info("Calendar successfully created");
        return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }

    @GetMapping()
    public ResponseEntity<List<CalendarResponse>> get() {
        log.info("Executing GET calendar from authenticated user");

        List<Calendar> calendarRecipes = getUserCalendarQuery.execute();
        List<CalendarResponse> response = calendarRecipes.stream().map(this::buildCalendarResponse).toList();

        return ResponseEntity.ok(response);
    }

    private CreateUserRecipeCalendarCommand.Command buildCommand(List<UserRecipeCalendarRequest> requests) {
        return CreateUserRecipeCalendarCommand.Command.builder()
                .calendarCommands(requests.stream().map(this::buildCalendar).toList())
                .build();
    }

    private CreateUserRecipeCalendarCommand.Command.CalendarCommand buildCalendar(UserRecipeCalendarRequest userRecipeCalendarRequest) {
        return CreateUserRecipeCalendarCommand.Command.CalendarCommand.builder()
                .dayId(userRecipeCalendarRequest.getDayId())
                .calendarRecipeCommands(userRecipeCalendarRequest.getRecipes().stream().map(this::buildRecipesCalendarCommand).toList())
                .build();
    }

    private CreateUserRecipeCalendarCommand.Command.CalendarRecipeCommand buildRecipesCalendarCommand(RecipeCalendarRequest recipeCalendarRequest) {
        return CreateUserRecipeCalendarCommand.Command.CalendarRecipeCommand.builder()
                .recipeId(recipeCalendarRequest.getRecipeId())
                .mealtypeId(recipeCalendarRequest.getMealTypeId())
                .build();
    }

    private CalendarResponse buildCalendarResponse(Calendar calendar) {
        return CalendarResponse.builder()
                .day(DayResponse.fromDomain(calendar.getDay()))
                .recipes(calendar.getRecipes().stream().map(this::buildCalendarRecipe).toList())
                .build();
    }

    private CalendarRecipeResponse buildCalendarRecipe(CalendarRecipe calendarRecipe) {
        return CalendarRecipeResponse.builder()
                .recipe(buildReducedRecipe(calendarRecipe.getRecipe()))
                .mealType(ParametricResponse.fromDomain(calendarRecipe.getMealType()))
                .build();
    }

    private RecipeResponse buildReducedRecipe(Recipe recipe) {
        return RecipeResponse.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .description(recipe.getDescription())
                .image(recipe.getImage())
                .build();
    }

}
