package com.cuoco.application.usecase;

import com.cuoco.application.exception.UnauthorizedException;
import com.cuoco.application.port.in.AuthenticateUserCommand;
import com.cuoco.application.port.out.GetUserByEmailRepository;
import com.cuoco.application.usecase.model.AuthenticatedUser;
import com.cuoco.application.usecase.model.User;
import com.cuoco.shared.model.ErrorDescription;
import com.cuoco.shared.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
public class AuthenticateUserUseCase implements AuthenticateUserCommand {

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
            throw new UnauthorizedException(ErrorDescription.UNAUTHORIZED.getValue());
        }

        String receivedJwt = authHeader.substring(7);
        String email = jwtUtil.extractEmail(receivedJwt);

        if (email == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            log.info("Token is not valid. The email is not present.");
            throw new UnauthorizedException(ErrorDescription.INVALID_TOKEN.getValue());
        }

        User user = getUserByEmailRepository.execute(email);

        if (user == null || !jwtUtil.validateToken(receivedJwt, user)) {
            log.info("Token or user with email {} are not valid", email);
            throw new UnauthorizedException(ErrorDescription.INVALID_TOKEN.getValue());
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
