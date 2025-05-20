package com.cuoco.application.usecase;

import com.cuoco.application.port.in.AuthenticateUserCommand;
import com.cuoco.application.port.out.GetUserByUsernameRepository;
import com.cuoco.application.usecase.model.User;
import com.cuoco.shared.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class AuthenticateUserUseCase implements AuthenticateUserCommand {

    private final GetUserByUsernameRepository getUserByUsernameRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthenticateUserUseCase(
            GetUserByUsernameRepository getUserRepository,
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil
    ) {
        this.getUserByUsernameRepository = getUserRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public User execute(Command command) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(command.getUser().getUsername(), command.getUser().getPassword())
            );
        } catch (Exception e) {
            // Puedes usar logger en lugar de imprimir
            System.out.println("Autenticación fallida: " + e.getMessage());
            throw e; // o lanzar una excepción personalizada
        }

        User userWithDetails = getUserByUsernameRepository.execute(command.getUser().getUsername());
        userWithDetails.setToken(jwtUtil.generateToken(userWithDetails));

        return userWithDetails;
    }
}
