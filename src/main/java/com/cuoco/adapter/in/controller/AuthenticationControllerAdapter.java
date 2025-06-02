package com.cuoco.adapter.in.controller;

import com.cuoco.application.port.in.SignInUserCommand;
import com.cuoco.application.port.in.CreateUserCommand;
import com.cuoco.application.usecase.model.AuthenticatedUser;
import com.cuoco.application.usecase.model.User;
import com.cuoco.adapter.in.controller.model.AuthRequest;
import com.cuoco.adapter.in.controller.model.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/auth")
public class AuthenticationControllerAdapter {

    static final Logger log = LoggerFactory.getLogger(AuthenticationControllerAdapter.class);

    private final SignInUserCommand signInUserCommand;
    private final CreateUserCommand createUserCommand;

    public AuthenticationControllerAdapter(SignInUserCommand signInUserCommand, CreateUserCommand createUserCommand) {
        this.signInUserCommand = signInUserCommand;
        this.createUserCommand = createUserCommand;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) throws Exception {

        log.info("Executing POST login for username {}", request.getName());

        AuthenticatedUser authenticatedUser = signInUserCommand.execute(buildAuthenticationCommand(request));
        AuthResponse response = new AuthResponse(authenticatedUser.getToken());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        log.info("Executing POST register with email {}", request.getEmail());

        createUserCommand.execute(buildCreateCommand(request));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private SignInUserCommand.Command buildAuthenticationCommand(AuthRequest request) {
        return new SignInUserCommand.Command(buildUser(request));
    }

    private CreateUserCommand.Command buildCreateCommand(AuthRequest request) {
        return new CreateUserCommand.Command(buildUser(request));
    }

    private User buildUser(AuthRequest request) {
        return new User(
                null,
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                LocalDate.now(),
                "Free",
                true,
                request.getCookLevel(),
                request.getDiet()
        );
    }
}
