package com.cuoco.adapter.in.controller;

import com.cuoco.application.port.in.UserRecipeCommand;
import com.cuoco.application.usecase.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/recipes")
public class UserRecipeControllerAdapter {

    static final Logger log = LoggerFactory.getLogger(UserRecipeControllerAdapter.class);

    private UserRecipeCommand userRecipeCommand;

    public UserRecipeControllerAdapter(UserRecipeCommand userRecipeCommand) {
        this.userRecipeCommand = userRecipeCommand;
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> save(@PathVariable Long id) {
        try {
            log.info("Executing save recipe");


            Boolean saved = userRecipeCommand.execute(buildRequestToCommand(id));

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

    private UserRecipeCommand.Command buildRequestToCommand(Long id) throws Exception {

        User user=null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof User){
            user = (User) principal;
            log.info("User: {}", user.getName());
        }

        if(user==null) {
            throw new Exception("User not found. Please log in to save a recipe.");
        }
        return new UserRecipeCommand.Command(
                user,
                id
        );
    }
}
