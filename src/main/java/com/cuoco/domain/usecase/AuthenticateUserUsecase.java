package com.cuoco.domain.usecase;

import com.cuoco.domain.model.User;
import com.cuoco.domain.port.repository.GetUserByUsernameRepository;
import com.cuoco.shared.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class AuthenticateUserUsecase {

    private final GetUserByUsernameRepository getUserByUsernameRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthenticateUserUsecase(
            GetUserByUsernameRepository getUserRepository,
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil
    ) {
        this.getUserByUsernameRepository = getUserRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public User execute(User user) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        User userWithDetails = getUserByUsernameRepository.execute(user.getUsername());
        userWithDetails.setToken(jwtUtil.generatdeToken(userWithDetails));

        return userWithDetails;
    }
}
