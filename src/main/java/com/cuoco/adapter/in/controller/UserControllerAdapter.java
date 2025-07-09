package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.ParametricResponse;
import com.cuoco.adapter.in.controller.model.UpdateUserRequest;
import com.cuoco.adapter.in.controller.model.UserPreferencesResponse;
import com.cuoco.adapter.in.controller.model.UserResponse;
import com.cuoco.application.port.in.UpdateUserProfileCommand;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.User;
import com.cuoco.shared.GlobalExceptionHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@Tag(name = "User", description = "Manipulate user data")
public class UserControllerAdapter {

    private final UpdateUserProfileCommand updateUserProfileCommand;

    public UserControllerAdapter(UpdateUserProfileCommand updateUserProfileCommand) {
        this.updateUserProfileCommand = updateUserProfileCommand;
    }

    @PatchMapping()
    @Operation(summary = "Update the current user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Return the updated user data"
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
    public ResponseEntity<UserResponse> updateProfile(@RequestBody UpdateUserRequest request) {
        log.info("Executing PATCH for user update");

        User user = updateUserProfileCommand.execute(buildUpdateCommand(request));
        UserResponse userResponse = buildUserResponse(user);

        return ResponseEntity.ok(userResponse);
    }

    private UpdateUserProfileCommand.Command buildUpdateCommand(UpdateUserRequest request) {
        return UpdateUserProfileCommand.Command.builder()
                .name(request.getName())
                .planId(request.getPlanId())
                .cookLevelId(request.getCookLevelId())
                .dietId(request.getDietId())
                .dietaryNeeds(request.getDietaryNeeds())
                .allergies(request.getAllergies())
                .build();
    }

    private UserResponse buildUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .plan(ParametricResponse.fromDomain(user.getPlan()))
                .preferences(UserPreferencesResponse.fromDomain(user.getPreferences()))
                .dietaryNeeds(buildDietaryNeeds(user.getDietaryNeeds()))
                .allergies(buildAllergies(user.getAllergies()))
                .build();
    }

    private List<ParametricResponse> buildDietaryNeeds(List<DietaryNeed> dietaryNeeds) {
        return dietaryNeeds != null && !dietaryNeeds.isEmpty() ? dietaryNeeds.stream().map(ParametricResponse::fromDomain).toList() : null;
    }

    private List<ParametricResponse> buildAllergies(List<Allergy> allergies) {
        return allergies != null && !allergies.isEmpty() ? allergies.stream().map(ParametricResponse::fromDomain).toList() : null;
    }

}