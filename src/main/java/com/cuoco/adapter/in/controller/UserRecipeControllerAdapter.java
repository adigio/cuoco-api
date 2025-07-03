package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.RecipeResponse;
import com.cuoco.application.port.in.CreateUserRecipeCommand;
import com.cuoco.application.port.in.DeleteUserRecipeCommand;
import com.cuoco.application.port.in.GetAllUserRecipesQuery;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.shared.GlobalExceptionHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users/recipes")
@Tag(name = "User favourites recipes", description = "Manipulate favourites recipes saved from the user")
public class UserRecipeControllerAdapter {

    static final Logger log = LoggerFactory.getLogger(UserRecipeControllerAdapter.class);

    private final CreateUserRecipeCommand createUserRecipeCommand;
    private final GetAllUserRecipesQuery getAllUserRecipesQuery;
    private final DeleteUserRecipeCommand deleteUserRecipeCommand;

    public UserRecipeControllerAdapter(
            CreateUserRecipeCommand createUserRecipeCommand,
            GetAllUserRecipesQuery getAllUserRecipesQuery,
            DeleteUserRecipeCommand deleteUserRecipeCommand
    ) {
        this.createUserRecipeCommand = createUserRecipeCommand;
        this.getAllUserRecipesQuery = getAllUserRecipesQuery;
        this.deleteUserRecipeCommand = deleteUserRecipeCommand;
    }

    @PostMapping("/{id}")
    @Operation(summary = "Creates new user favorite recipes")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "The recipe was successfully associated to the user"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recipe not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Recipe is already associated to the user",
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
    public ResponseEntity<?> save(@PathVariable(name = "id") Long recipeId) {
        log.info("Executing POST for associate recipe to user");

        createUserRecipeCommand.execute(buildCreateCommand(recipeId));

        log.info("Successfully associated recipe to user");
        return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }

    @GetMapping
    @Operation(summary = "Get all the favorite recipes for the current user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Retrieve all the recipes associated to the authenticated user"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recipe not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Recipe is already associated to the user",
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
    public ResponseEntity<List<RecipeResponse>> getAll() {
        log.info("Executing GET for get all user recipes");

        List<Recipe> recipes = getAllUserRecipesQuery.execute();

        List<RecipeResponse> response = recipes.stream().map(this::buildRecipeResponse).toList();

        log.info("Successfully retrieved all user recipes");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes one favorite recipe from the current user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "The recipe was successfully deleted from the user. If the recipe doesn't exists, it doesn't make any changes."
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
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long recipeId) {
        log.info("Executing DELETE for remove recipe to user");

        deleteUserRecipeCommand.execute(buildDeleteCommand(recipeId));
        return ResponseEntity.noContent().build();
    }

    private CreateUserRecipeCommand.Command buildCreateCommand(Long recipeId) {
        return CreateUserRecipeCommand.Command.builder().recipeId(recipeId).build();
    }

    private DeleteUserRecipeCommand.Command buildDeleteCommand(Long recipeId) {
        return DeleteUserRecipeCommand.Command.builder().recipeId(recipeId).build();
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
