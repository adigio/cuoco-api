package com.cuoco.presentation.controller;

import com.cuoco.domain.model.User;
import com.cuoco.domain.usecase.AuthenticateUserUsecase;
import com.cuoco.domain.usecase.CreateUserUsecase;
import com.cuoco.presentation.controller.model.AuthRequest;
import com.cuoco.presentation.controller.model.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticateUserUsecase authenticateUserUsecase;
    private final CreateUserUsecase createUserUsecase;

    public AuthController(
            CreateUserUsecase createUserUsecase,
            AuthenticateUserUsecase authenticateUserUsecase
    ) {
        this.createUserUsecase = createUserUsecase;
        this.authenticateUserUsecase = authenticateUserUsecase;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) throws Exception {

        User userAuthenticated = authenticateUserUsecase.execute(buildUser(request));
        AuthResponse response = new AuthResponse(userAuthenticated.getToken());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {

        User user = createUserUsecase.execute(buildUser(request));

        return ResponseEntity.ok("Usuario registrado exitosamente.");
    }

    private User buildUser(AuthRequest request) {
        return new User(
                null,
                null,
                null,
                request.getUsername(),
                request.getPassword(),
                null
        );
    }
}
