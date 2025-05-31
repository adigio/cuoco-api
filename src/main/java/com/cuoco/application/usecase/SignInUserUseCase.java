package com.cuoco.application.usecase;

import com.cuoco.application.port.in.SignInUserCommand;
import com.cuoco.application.port.out.GetUserByUsernameRepository;
import com.cuoco.application.usecase.model.AuthenticatedUser;
import com.cuoco.application.usecase.model.User;
import com.cuoco.shared.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class SignInUserUseCase implements SignInUserCommand {

    static final Logger log = LoggerFactory.getLogger(SignInUserUseCase.class);

    private final GetUserByUsernameRepository getUserByUsernameRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public SignInUserUseCase(
            GetUserByUsernameRepository getUserRepository,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder) {
        this.getUserByUsernameRepository = getUserRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthenticatedUser execute(Command command) {

        log.info("Executing signin user use case for username: {}", command.getUser().getNombre());

        User user = getUserByUsernameRepository.execute(command.getUser().getNombre());

        if(!passwordEncoder.matches(command.getUser().getPassword(), user.getPassword())) {
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
