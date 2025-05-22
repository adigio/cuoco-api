package com.cuoco.adapter.in.controller;

import com.cuoco.application.port.in.AuthenticateUserCommand;
import com.cuoco.application.port.in.CreateUserCommand;
import com.cuoco.application.usecase.model.User;
import com.cuoco.adapter.in.controller.model.AuthRequest;
import com.cuoco.adapter.in.controller.model.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationControllerAdapter {

    private final AuthenticateUserCommand authenticateUserCommand;
    private final CreateUserCommand createUserCommand;

    public AuthenticationControllerAdapter(AuthenticateUserCommand authenticateUserCommand, CreateUserCommand createUserCommand) {
        this.authenticateUserCommand = authenticateUserCommand;
        this.createUserCommand = createUserCommand;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) throws Exception {

        User userAuthenticated = authenticateUserCommand.execute(buildAuthenticationCommand(request));
        AuthResponse response = new AuthResponse(userAuthenticated.getToken());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {

        User user = createUserCommand.execute(buildCreateCommand(request));

        return ResponseEntity.ok("Usuario registrado exitosamente.");
    }

    private AuthenticateUserCommand.Command buildAuthenticationCommand(AuthRequest request) {
        return new AuthenticateUserCommand.Command(
                new User(
                        null,
                        null,
                        null,
                        request.getUsername(),
                        request.getPassword(),
                        null
                )
        );
    }

    private CreateUserCommand.Command buildCreateCommand(AuthRequest request) {
        return new CreateUserCommand.Command(
                new User(
                        null,
                        null,
                        null,
                        request.getUsername(),
                        request.getPassword(),
                        null
                )
        );
    }
}
