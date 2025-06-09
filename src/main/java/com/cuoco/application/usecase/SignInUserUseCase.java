package com.cuoco.application.usecase;

import com.cuoco.application.port.in.SignInUserCommand;
import com.cuoco.application.port.out.GetUserByEmailRepository;
import com.cuoco.application.usecase.model.AuthenticatedUser;
import com.cuoco.application.usecase.model.User;
import com.cuoco.shared.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class SignInUserUseCase implements SignInUserCommand {

    static final Logger log = LoggerFactory.getLogger(SignInUserUseCase.class);

    private final GetUserByEmailRepository getUserByEmailRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public SignInUserUseCase(
            GetUserByEmailRepository getUserRepository,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder) {
        this.getUserByEmailRepository = getUserRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthenticatedUser execute(Command command) {

        log.info("Executing signin user use case for email: {}", command.getEmail());

        User user = getUserByEmailRepository.execute(command.getEmail());


        if(!passwordEncoder.matches(command.getPassword(), user.getPassword())) {
            log.info("Invalid credentials");
            throw new RuntimeException("Credenciales inválidas");
        }

        user.setPassword(null);
        AuthenticatedUser authenticatedUser = buildAuthenticatedUser(user);

        return authenticatedUser;
    }

    private AuthenticatedUser buildAuthenticatedUser(User user) {
        return new AuthenticatedUser(
                user,
                jwtUtil.generateToken(user),
                Collections.emptyList()
        );
    }
}
