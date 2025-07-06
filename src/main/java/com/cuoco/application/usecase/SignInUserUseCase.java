package com.cuoco.application.usecase;

import com.cuoco.application.exception.ForbiddenException;
import com.cuoco.application.port.in.SignInUserCommand;
import com.cuoco.application.port.out.GetUserByEmailRepository;
import com.cuoco.application.usecase.model.AuthenticatedUser;
import com.cuoco.application.usecase.model.User;
import com.cuoco.shared.model.ErrorDescription;
import com.cuoco.application.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
public class SignInUserUseCase implements SignInUserCommand {

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
            throw new ForbiddenException(ErrorDescription.INVALID_CREDENTIALS.getValue());
        }

        user.setPassword(null);

        return buildAuthenticatedUser(user);
    }

    private AuthenticatedUser buildAuthenticatedUser(User user) {
        return new AuthenticatedUser(
                user,
                jwtUtil.generateToken(user),
                Collections.emptyList()
        );
    }
}
