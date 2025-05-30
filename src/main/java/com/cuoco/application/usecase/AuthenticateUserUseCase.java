package com.cuoco.application.usecase;

import com.cuoco.application.port.in.AuthenticateUserCommand;
import com.cuoco.application.port.out.GetUserByUsernameRepository;
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

    private final JwtUtil jwtUtil;
    private final GetUserByUsernameRepository getUserByUsernameRepository;

    public AuthenticateUserUseCase(JwtUtil jwtUtil, GetUserByUsernameRepository getUserByUsernameRepository) {
        this.jwtUtil = jwtUtil;
        this.getUserByUsernameRepository = getUserByUsernameRepository;
    }

    @Override
    public AuthenticatedUser execute(Command command) {

        String authHeader = command.getAuthHeader();

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        String jwt = authHeader.substring(7);
        String username = jwtUtil.extractUsername(jwt);

        if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            return null;
        }

        User user = getUserByUsernameRepository.execute(username);

        if (user == null || !jwtUtil.validateToken(jwt, user)) {
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
