package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.out.mail.token.TokenService;
import com.cuoco.application.port.in.ConfirmUserCommand;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserConfirmationController {
    private final TokenService tokenService;
    private final ConfirmUserCommand confirmUserCommand;

    @GetMapping("/confirm")
    @Operation(summary = "Confirmación de cuenta de usuario por token")
    public ResponseEntity<String> confirmUser(@RequestParam String token) {
        Long userId = tokenService.validateToken(token);
        if (userId == null) {
            return ResponseEntity.badRequest().body("El enlace es inválido o ha expirado.");
        }

        confirmUserCommand.execute(userId);
        return ResponseEntity.ok("¡Cuenta confirmada con éxito! Ya podés iniciar sesión.");
    }
}
