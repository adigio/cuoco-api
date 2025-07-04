package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.IngredientResponse;
import com.cuoco.adapter.in.controller.model.MealPrepResponse;
import com.cuoco.adapter.in.controller.model.RecipeResponse;
import com.cuoco.adapter.in.controller.model.StepResponse;
import com.cuoco.application.port.in.CreateUserMealPrepCommand;
import com.cuoco.application.port.in.DeleteUserMealPrepCommand;
import com.cuoco.application.port.in.GetAllUserMealPrepsQuery;
import com.cuoco.application.usecase.model.MealPrep;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.shared.GlobalExceptionHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/meal-preps")
@Tag(name = "User favourites meal preps", description = "Manipulate favourites meal preps saved from the user")
public class UserMealPrepControllerAdapter {

    private final CreateUserMealPrepCommand createUserMealPrepCommand;
    private final GetAllUserMealPrepsQuery getAllUserMealPrepsQuery;
    private final DeleteUserMealPrepCommand deleteUserMealPrepCommand;

    public UserMealPrepControllerAdapter(
            CreateUserMealPrepCommand createUserMealPrepCommand,
            GetAllUserMealPrepsQuery getAllUserMealPrepsQuery,
            DeleteUserMealPrepCommand deleteUserMealPrepCommand
    ) {
        this.createUserMealPrepCommand = createUserMealPrepCommand;
        this.getAllUserMealPrepsQuery = getAllUserMealPrepsQuery;
        this.deleteUserMealPrepCommand = deleteUserMealPrepCommand;
    }

    @PostMapping("/{id}")
    @Operation(summary = "Creates new user favorite meal prep")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "The meal prep was successfully associated to the user"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Meal prep not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Meal prep is already associated to the user",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ApiErrorResponse.class)
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
    public ResponseEntity<?> save(@PathVariable(name = "id") Long mealPrepId) {
        log.info("Executing POST for associate meal prep to user");

        createUserMealPrepCommand.execute(buildCreateCommand(mealPrepId));

        log.info("Successfully associated meal prep to user");
        return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }

    @GetMapping
    @Operation(summary = "Get all the favorite meal preps for the current user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Retrieve all the meal prep associated to the authenticated user"
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
    public ResponseEntity<List<MealPrepResponse>> getAll() {
        log.info("Executing GET for retrieve all user meal preps");

        List<MealPrep> mealPreps = getAllUserMealPrepsQuery.execute();

        List<MealPrepResponse> response = mealPreps.stream().map(this::buildResponse).toList();

        log.info("Successfully retrieved all user meal preps");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes one favorite meal prep from the current user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "The meal prep was successfully deleted from the user. If the meal prep is not associated, it doesn't make any changes."
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
    public ResponseEntity<?> delete(@PathVariable Long id) {
        log.info("Executing DELETE for remove meal prep to user");

        deleteUserMealPrepCommand.execute(buildDeleteCommand(id));
        return ResponseEntity.noContent().build();
    }

    private CreateUserMealPrepCommand.Command buildCreateCommand(Long id) {
        return CreateUserMealPrepCommand.Command.builder().id(id).build();
    }

    private DeleteUserMealPrepCommand.Command buildDeleteCommand(Long id) {
        return DeleteUserMealPrepCommand.Command.builder().id(id).build();
    }

    private MealPrepResponse buildResponse(MealPrep mealPrep) {
        return MealPrepResponse.builder()
                .id(mealPrep.getId())
                .title(mealPrep.getTitle())
                .estimatedCookingTime(mealPrep.getEstimatedCookingTime())
                .servings(mealPrep.getServings())
                .freeze(mealPrep.getFreeze())
                .steps(mealPrep.getSteps().stream().map(StepResponse::fromDomain).toList())
                .recipes(mealPrep.getRecipes().stream().map(this::buildRecipeResponse).toList())
                .ingredients(mealPrep.getIngredients().stream().map(IngredientResponse::fromDomain).toList())
                .build();
    }

    private RecipeResponse buildRecipeResponse(Recipe recipe) {
        return RecipeResponse.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .subtitle(recipe.getSubtitle())
                .description(recipe.getDescription())
                .image(recipe.getImage())
                .build();
    }
}
