package com.cuoco.application.usecase;

import com.cuoco.application.port.in.AuthenticateUserCommand;
import com.cuoco.application.port.out.GetUserByEmailRepository;
import com.cuoco.application.usecase.model.AuthenticatedUser;
import com.cuoco.application.usecase.model.User;
import com.cuoco.shared.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AuthenticateUserUseCase implements AuthenticateUserCommand {

    static final Logger log = LoggerFactory.getLogger(AuthenticateUserUseCase.class);

    static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;
    private final GetUserByEmailRepository getUserByEmailRepository;

    public AuthenticateUserUseCase(JwtUtil jwtUtil, GetUserByEmailRepository getUserByEmailRepository) {
        this.jwtUtil = jwtUtil;
        this.getUserByEmailRepository = getUserByEmailRepository;
    }

    @Override
    public AuthenticatedUser execute(Command command) {

        log.info("Executing authenticate user usecase");

        String authHeader = command.getAuthHeader();

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            log.info("User don't have a valid auth header");
            return null;
        }

        String receivedJwt = authHeader.substring(7);
        String email = jwtUtil.extractEmail(receivedJwt);

        if (email == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            log.info("Token is not valid. The email is not present.");
            return null;
        }

        User user = getUserByEmailRepository.execute(email);

        if (user == null || !jwtUtil.validateToken(receivedJwt, user)) {
            log.info("Token or user with email {} are not valid", email);
            return null;
        }

        return buildAuthenticatedUser(user);
    }

    private AuthenticatedUser buildAuthenticatedUser(User user) {
        return new AuthenticatedUser(
                user,
                null,
                Collections.emptyList()
        );
    }
}
