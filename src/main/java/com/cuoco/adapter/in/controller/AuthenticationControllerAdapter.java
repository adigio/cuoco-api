package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.AuthDataResponse;
import com.cuoco.adapter.in.controller.model.AuthRequest;
import com.cuoco.adapter.in.controller.model.AuthResponse;
import com.cuoco.adapter.in.controller.model.ParametricResponse;
import com.cuoco.adapter.in.controller.model.UserPreferencesResponse;
import com.cuoco.adapter.in.controller.model.UserRequest;
import com.cuoco.adapter.in.controller.model.UserResponse;
import com.cuoco.adapter.out.mail.EmailService;
import com.cuoco.application.port.in.ActivateUserCommand;
import com.cuoco.application.port.in.CreateUserCommand;
import com.cuoco.application.port.in.SignInUserCommand;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.AuthenticatedUser;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.User;
import com.cuoco.shared.GlobalExceptionHandler;
import com.cuoco.shared.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Operations related to authenticate users")
public class AuthenticationControllerAdapter {

    private final SignInUserCommand signInUserCommand;
    private final CreateUserCommand createUserCommand;
    private final ActivateUserCommand activateUserCommand;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;


    public AuthenticationControllerAdapter(
            SignInUserCommand signInUserCommand,
            CreateUserCommand createUserCommand,
            ActivateUserCommand activateUserCommand,
            EmailService emailService,
            JwtUtil jwtUtil
    ) {
        this.signInUserCommand = signInUserCommand;
        this.createUserCommand = createUserCommand;
        this.activateUserCommand = activateUserCommand;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
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

    private AuthResponse buildAuthResponse(AuthenticatedUser authenticatedUser) {
        return AuthResponse.builder()
                .data(AuthDataResponse.builder()
                        .user(buildUserResponse(authenticatedUser.getUser(), authenticatedUser.getToken()))
                        .build())
                .build();
    }

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

        // Generar link de confirmación (esto debería venir de una configuración)
        String confirmationLink = "http://localhost:8080/auth/confirm?token=" + generateConfirmationToken(user);

        // Enviar correo de confirmación
        emailService.sendConfirmationEmail(user.getEmail(), confirmationLink);

        UserResponse userResponse = buildUserResponse(user, null);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @GetMapping("/confirm")
    @Operation(summary = "GET para confirmar el email del usuario")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Email confirmado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Token inválido o expirado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ApiErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<Void> confirmEmail(@RequestParam String token) {
        log.info("Ejecutando confirmación de email");

        String email = jwtUtil.extractEmail(token);
        activateUserCommand.execute(email);

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

    private String generateConfirmationToken(User user) {
        return jwtUtil.generateToken(user);
    }

}
