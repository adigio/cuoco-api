package com.cuoco.application.usecase;

import com.cuoco.application.exception.ForbiddenException;
import com.cuoco.application.exception.UnauthorizedException;
import com.cuoco.application.port.in.AuthenticateUserCommand;
import com.cuoco.application.port.out.GetUserByEmailRepository;
import com.cuoco.application.usecase.model.AuthenticatedUser;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.utils.JwtUtil;
import com.cuoco.shared.model.ErrorDescription;
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

        log.info("Executing user authentication usecase");

        String authHeader = command.getAuthHeader();

        if (authHeader == null) {
            log.info("Auth header is not present");
            throw new UnauthorizedException(ErrorDescription.NO_AUTH_TOKEN.getValue());
        }

        if (!authHeader.startsWith(BEARER_PREFIX)) {
            log.info("Don't have a valid auth token");
            throw new UnauthorizedException(ErrorDescription.INVALID_CREDENTIALS.getValue());
        }

        String receivedJwt = authHeader.substring(7);
        String email = jwtUtil.extractEmail(receivedJwt);

        if (email == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            log.info("Token is not valid: The email is not present.");
            throw new UnauthorizedException(ErrorDescription.INVALID_CREDENTIALS.getValue());
        }

        User user = getUserByEmailRepository.execute(email);

        if (user == null || !jwtUtil.validateToken(receivedJwt, user)) {
            log.info("Token or user with email {} are not valid or not exists", email);
            throw new UnauthorizedException(ErrorDescription.INVALID_CREDENTIALS.getValue());
        }

        if (user.getActive() != null && !user.getActive()) {
            log.info("User with email {} is not activated yet", email);
            throw new ForbiddenException(ErrorDescription.USER_NOT_ACTIVATED.getValue());
        }

        log.info("User authenticated with email {}", email);
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
