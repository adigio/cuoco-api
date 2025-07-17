package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.CalendarRecipeResponse;
import com.cuoco.adapter.in.controller.model.CalendarResponse;
import com.cuoco.adapter.in.controller.model.DayResponse;
import com.cuoco.adapter.in.controller.model.ParametricResponse;
import com.cuoco.adapter.in.controller.model.RecipeCalendarRequest;
import com.cuoco.adapter.in.controller.model.RecipeResponse;
import com.cuoco.adapter.in.controller.model.UserRecipeCalendarRequest;
import com.cuoco.application.port.in.CreateOrUpdateUserRecipeCalendarCommand;
import com.cuoco.application.port.in.GetUserCalendarQuery;
import com.cuoco.application.usecase.model.Calendar;
import com.cuoco.application.usecase.model.CalendarRecipe;
import com.cuoco.application.usecase.model.Day;
import com.cuoco.application.usecase.model.MealType;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.shared.GlobalExceptionHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/calendar")
@Tag(name = "User calendar", description = "Manipulates user planning calendar operations")
public class UserCalendarControllerAdapter {

    private final CreateOrUpdateUserRecipeCalendarCommand createOrUpdateUserRecipeCalendarCommand;
    private final GetUserCalendarQuery getUserCalendarQuery;

    public UserCalendarControllerAdapter(CreateOrUpdateUserRecipeCalendarCommand createOrUpdateUserRecipeCalendarCommand, GetUserCalendarQuery getUserCalendarQuery) {
        this.createOrUpdateUserRecipeCalendarCommand = createOrUpdateUserRecipeCalendarCommand;
        this.getUserCalendarQuery = getUserCalendarQuery;
    }

    @PutMapping()
    @Operation(summary = "Creates or update the user calendar")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Return NO CONTENT when the calendar was successfully created for the current user"
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Service unavailable",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ApiErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<?> save(@RequestBody @Valid List<UserRecipeCalendarRequest> requests) {
        log.info("Executing POST for user recipe calendar creation");

        createOrUpdateUserRecipeCalendarCommand.execute(buildCommand(requests));

        log.info("Calendar successfully created");
        return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }

    @GetMapping()
    @Operation(summary = "GET the user calendar")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Return the calendar for the current user",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CalendarResponse.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Service unavailable",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ApiErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<List<CalendarResponse>> get() {
        log.info("Executing GET calendar from authenticated user");

        List<Calendar> calendarRecipes = getUserCalendarQuery.execute();
        List<CalendarResponse> response = calendarRecipes.stream().map(this::buildCalendarResponse).toList();

        return ResponseEntity.ok(response);
    }

    private CreateOrUpdateUserRecipeCalendarCommand.Command buildCommand(List<UserRecipeCalendarRequest> requests) {
        return CreateOrUpdateUserRecipeCalendarCommand.Command.builder()
                .calendars(requests.stream().map(this::buildCalendars).toList())
                .build();
    }

    private Calendar buildCalendars(UserRecipeCalendarRequest userRecipeCalendarRequest) {
        return Calendar.builder()
                .day(Day.builder().id(userRecipeCalendarRequest.getDayId()).build())
                .recipes(userRecipeCalendarRequest.getRecipes().stream().map(this::buildCalendarRecipe).toList())
                .build();
    }

    private CalendarRecipe buildCalendarRecipe(RecipeCalendarRequest recipeCalendarRequest) {
        return CalendarRecipe.builder()
                .recipe(Recipe.builder().id(recipeCalendarRequest.getRecipeId()).build())
                .mealType(MealType.builder().id(recipeCalendarRequest.getMealTypeId()).build())
                .build();
    }

    private CalendarResponse buildCalendarResponse(Calendar calendar) {
        return CalendarResponse.builder()
                .day(DayResponse.fromDomain(calendar.getDay()))
                .recipes(calendar.getRecipes().stream().map(this::buildCalendarRecipeResponse).toList())
                .build();
    }

    private CalendarRecipeResponse buildCalendarRecipeResponse(CalendarRecipe calendarRecipe) {
        return CalendarRecipeResponse.builder()
                .recipe(buildReducedRecipeResponse(calendarRecipe.getRecipe()))
                .mealType(ParametricResponse.fromDomain(calendarRecipe.getMealType()))
                .build();
    }

    private RecipeResponse buildReducedRecipeResponse(Recipe recipe) {
        return RecipeResponse.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .description(recipe.getDescription())
                .image(recipe.getImage())
                .build();
    }

}
