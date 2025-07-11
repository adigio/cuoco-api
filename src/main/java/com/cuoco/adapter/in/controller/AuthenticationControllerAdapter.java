package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.AuthDataResponse;
import com.cuoco.adapter.in.controller.model.AuthRequest;
import com.cuoco.adapter.in.controller.model.AuthResponse;
import com.cuoco.adapter.in.controller.model.ParametricResponse;
import com.cuoco.adapter.in.controller.model.UserPreferencesResponse;
import com.cuoco.adapter.in.controller.model.UserRequest;
import com.cuoco.adapter.in.controller.model.UserResponse;
import com.cuoco.application.port.in.CreateUserCommand;
import com.cuoco.application.port.in.SignInUserCommand;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.AuthenticatedUser;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserPreferences;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthenticationControllerAdapter {

    static final Logger log = LoggerFactory.getLogger(AuthenticationControllerAdapter.class);

    private final SignInUserCommand signInUserCommand;
    private final CreateUserCommand createUserCommand;


    public AuthenticationControllerAdapter(
            SignInUserCommand signInUserCommand,
            CreateUserCommand createUserCommand
    ) {
        this.signInUserCommand = signInUserCommand;
        this.createUserCommand = createUserCommand;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {

        log.info("Executing POST login for email {}", request.getEmail());

        AuthenticatedUser authenticatedUser = signInUserCommand.execute(buildAuthenticationCommand(request));
        AuthResponse response = buildAuthResponse(authenticatedUser);

        return ResponseEntity.ok(response);
    }

    private AuthResponse buildAuthResponse(AuthenticatedUser authenticatedUser) {
        return AuthResponse.builder()
                .data(AuthDataResponse.builder()
                        .user(buildUserResponse(authenticatedUser.getUser(), authenticatedUser.getToken()))
                        .build())
                .build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRequest request) {
        log.info("Executing POST register with email {}", request.getEmail());

        User user = createUserCommand.execute(buildCreateCommand(request));

        UserResponse userResponse = buildUserResponse(user, null);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    private SignInUserCommand.Command buildAuthenticationCommand(AuthRequest request) {
        return new SignInUserCommand.Command(
                request.getEmail(),
                request.getPassword()
        );
    }

    private CreateUserCommand.Command buildCreateCommand(UserRequest request) {
        return new CreateUserCommand.Command(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getPlanId(),
                request.getCookLevelId(),
                request.getDietId(),
                request.getDietaryNeeds(),
                request.getAllergies()
        );
    }

    private UserResponse buildUserResponse(User user, String token) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .token(token)
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
