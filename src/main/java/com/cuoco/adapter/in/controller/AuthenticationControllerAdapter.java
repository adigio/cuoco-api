package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.AuthDataResponse;
import com.cuoco.adapter.in.controller.model.AuthOperationRequest;
import com.cuoco.adapter.in.controller.model.AuthRequest;
import com.cuoco.adapter.in.controller.model.AuthResponse;
import com.cuoco.adapter.in.controller.model.ChangePasswordRequest;
import com.cuoco.adapter.in.controller.model.ParametricResponse;
import com.cuoco.adapter.in.controller.model.UserPreferencesResponse;
import com.cuoco.adapter.in.controller.model.UserRequest;
import com.cuoco.adapter.in.controller.model.UserResponse;
import com.cuoco.application.port.in.ActivateUserCommand;
import com.cuoco.application.port.in.ChangeUserPasswordCommand;
import com.cuoco.application.port.in.CreateUserCommand;
import com.cuoco.application.port.in.ResendUserActivationEmailCommand;
import com.cuoco.application.port.in.ResetUserPasswordConfirmationCommand;
import com.cuoco.application.port.in.SignInUserCommand;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.AuthenticatedUser;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.User;
import com.cuoco.shared.GlobalExceptionHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Operations related to authenticate users")
public class AuthenticationControllerAdapter {

    private final CreateUserCommand createUserCommand;
    private final SignInUserCommand signInUserCommand;
    private final ActivateUserCommand activateUserCommand;
    private final ResendUserActivationEmailCommand resendUserActivationEmailCommand;
    private final ResetUserPasswordConfirmationCommand resetUserPasswordConfirmationCommand;
    private final ChangeUserPasswordCommand changeUserPasswordCommand;

    @PostMapping("/register")
    @Operation(summary = "POST for user creation with basic data and preferences")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Return created user",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Required parameter is not present or some parameter is not valid ",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "The user already exists",
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
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRequest request) {
        log.info("Executing POST register with email {}", request.getEmail());

        User user = createUserCommand.execute(buildCreateCommand(request));
        UserResponse userResponse = buildUserResponse(user, null);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PostMapping("/login")
    @Operation(summary = "POST for user authentication with email and password")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Return user data with token",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Invalid credentials",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "The request can't be procesed due to an error",
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
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {

        log.info("Executing POST login for email {}", request.getEmail());

        AuthenticatedUser authenticatedUser = signInUserCommand.execute(buildAuthenticationCommand(request));
        AuthResponse response = buildAuthResponse(authenticatedUser);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/activate")
    @Operation(summary = "GET for confirm user email")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Email confirmed successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid token or expired",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ApiErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<?> confirmEmail(@RequestParam String token) {
        log.info("Executing POST email confirmation for token {}", token);

        ActivateUserCommand.Command command = ActivateUserCommand.Command.builder().token(token).build();

        activateUserCommand.execute(command);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/activate/resend-email")
    public ResponseEntity<?> resendEmailConfirmation(@RequestBody @Valid AuthOperationRequest request) {
        log.info("Executing POST for resend email confirmation");

        resendUserActivationEmailCommand.execute(request.getEmail());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/reset")
    public ResponseEntity<UserResponse> resetPasswordConfirmation(@RequestBody @Valid AuthOperationRequest request) {
        log.info("Executing POST for require reset password");

        resetUserPasswordConfirmationCommand.execute(request.getEmail());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/password")
    public ResponseEntity<UserResponse> resetPassword(@RequestParam String token, @RequestBody @Valid ChangePasswordRequest request) {
        log.info("Executing POST for change password without authentication");

        ChangeUserPasswordCommand.Command command = ChangeUserPasswordCommand.Command.builder()
                .token(token)
                .password(request.getPassword())
                .build();

        changeUserPasswordCommand.execute(command);

        return ResponseEntity.ok().build();
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

    private AuthResponse buildAuthResponse(AuthenticatedUser authenticatedUser) {
        return AuthResponse.builder()
                .data(AuthDataResponse.builder()
                        .user(buildUserResponse(authenticatedUser.getUser(), authenticatedUser.getToken()))
                        .build())
                .build();
    }

    private UserResponse buildUserResponse(User user, String token) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .token(token)
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
