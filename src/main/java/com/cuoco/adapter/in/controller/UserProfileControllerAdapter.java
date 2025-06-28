package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.ParametricResponse;
import com.cuoco.adapter.in.controller.model.UpdateUserProfileRequest;
import com.cuoco.adapter.in.controller.model.UserPreferencesResponse;
import com.cuoco.adapter.in.controller.model.UserResponse;
import com.cuoco.application.port.in.UpdateUserProfileCommand;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserPreferences;
import com.cuoco.shared.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/profile")
public class UserProfileControllerAdapter {

    private final UpdateUserProfileCommand updateUserProfileCommand;
    private final JwtUtil jwtUtil;

    public UserProfileControllerAdapter(UpdateUserProfileCommand updateUserProfileCommand, JwtUtil jwtUtil) {
        this.updateUserProfileCommand = updateUserProfileCommand;
        this.jwtUtil = jwtUtil;
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UpdateUserProfileRequest request) {

        log.info("Executing PUT profile update");

        String receivedJwt = authHeader.substring(7);
        String userEmail = jwtUtil.extractEmail(receivedJwt);

        User user = updateUserProfileCommand.execute(buildUpdateCommand(request, userEmail));
        UserResponse userResponse = buildUserResponse(user);

        return ResponseEntity.ok(userResponse);
    }

    private UpdateUserProfileCommand.Command buildUpdateCommand(UpdateUserProfileRequest request, String userEmail) {
        return UpdateUserProfileCommand.Command.builder()
                .userEmail(userEmail)
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
                .token(null)
                .plan(ParametricResponse.builder()
                        .id(user.getPlan().getId())
                        .description(user.getPlan().getDescription())
                        .build())
                .preferences(buildUserPreferencesResponse(user.getPreferences()))
                .dietaryNeeds(buildDietaryNeeds(user.getDietaryNeeds()))
                .allergies(buildAllergies(user.getAllergies()))
                .build();
    }

    private List<ParametricResponse> buildDietaryNeeds(List<DietaryNeed> dietaryNeeds) {
        if(dietaryNeeds != null && !dietaryNeeds.isEmpty()) {
            return dietaryNeeds.stream().map(dietaryNeed -> ParametricResponse.builder()
                    .id(dietaryNeed.getId())
                    .description(dietaryNeed.getDescription())
                    .build()).toList();
        } else return null;
    }

    private List<ParametricResponse> buildAllergies(List<Allergy> allergies) {
        if(allergies != null && !allergies.isEmpty()) {
            return allergies.stream().map(allergy -> ParametricResponse.builder()
                    .id(allergy.getId())
                    .description(allergy.getDescription())
                    .build()
            ).toList();
        } else return null;
    }

    private UserPreferencesResponse buildUserPreferencesResponse(UserPreferences preferences) {
        return UserPreferencesResponse.builder()
                .cookLevel(ParametricResponse.builder()
                        .id(preferences.getCookLevel().getId())
                        .description(preferences.getCookLevel().getDescription())
                        .build())
                .diet(ParametricResponse.builder()
                        .id(preferences.getDiet().getId())
                        .description(preferences.getDiet().getDescription())
                        .build())
                .build();
    }
}