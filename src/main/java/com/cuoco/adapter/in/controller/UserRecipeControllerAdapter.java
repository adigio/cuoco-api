package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.UserRecipesResponse;
import com.cuoco.application.port.in.GetUserRecipeCommand;
import com.cuoco.application.port.in.SaveUserRecipeCommand;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserRecipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users/recipes")
public class UserRecipeControllerAdapter {

    static final Logger log = LoggerFactory.getLogger(UserRecipeControllerAdapter.class);

    private SaveUserRecipeCommand saveUserRecipeCommand;

    private GetUserRecipeCommand getUserRecipeCommand;

    public UserRecipeControllerAdapter(SaveUserRecipeCommand saveUserRecipeCommand, GetUserRecipeCommand getUserRecipeCommand) {
        this.saveUserRecipeCommand = saveUserRecipeCommand;
        this.getUserRecipeCommand = getUserRecipeCommand;
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> save(@PathVariable Long id) {
        try {
            log.info("Executing save recipe");


            Boolean saved = saveUserRecipeCommand.execute(buildRequestToCommand(id));

            if(!saved){
                log.info("Error to save a recipe");
                throw new Exception();
            }

            log.info("Recipe is a favourite");
            return ResponseEntity.ok(saved);


        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error trying to save the recipe: " + e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getFavourites() {
        List<UserRecipe> recipes = getUserRecipeCommand.execute();
        List<UserRecipesResponse> response = recipes.stream().map(this::buildResponseFromRecipes).toList();
        return ResponseEntity.ok(response);
    }

    private UserRecipesResponse buildResponseFromRecipes(UserRecipe userRecipe) {
        return UserRecipesResponse.builder()
                .id(userRecipe.getId())
                .user(userRecipe.getUser())
                .recipe(userRecipe.getRecipe())
                .favorite(userRecipe.isFavorite())
                .build();
    }


    private SaveUserRecipeCommand.Command buildRequestToCommand(Long id) throws Exception {

        User user=null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof User){
            user = (User) principal;
            log.info("User: {}", user.getName());
        }

        if(user==null) {
            throw new Exception("User not found. Please log in to save a recipe.");
        }
        return new SaveUserRecipeCommand.Command(
                user,
                id
        );
    }
}
