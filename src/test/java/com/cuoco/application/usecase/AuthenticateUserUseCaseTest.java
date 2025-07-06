package com.cuoco.application.usecase;

import com.cuoco.application.exception.UnauthorizedException;
import com.cuoco.application.port.in.AuthenticateUserCommand;
import com.cuoco.application.port.out.GetUserByEmailRepository;
import com.cuoco.application.usecase.model.AuthenticatedUser;
import com.cuoco.application.usecase.model.User;
import com.cuoco.factory.domain.UserFactory;
import com.cuoco.shared.model.ErrorDescription;
import com.cuoco.application.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthenticateUserUseCaseTest {

    private JwtUtil jwtUtil;
    private GetUserByEmailRepository getUserByEmailRepository;
    private AuthenticateUserUseCase useCase;

    @BeforeEach
    void setup() {
        jwtUtil = mock(JwtUtil.class);
        getUserByEmailRepository = mock(GetUserByEmailRepository.class);
        useCase = new AuthenticateUserUseCase(jwtUtil, getUserByEmailRepository);
    }

    @Test
    void GIVEN_valid_token_WHEN_execute_THEN_returns_authenticated_user() {
        String token = "Bearer valid.jwt.token";
        String email = "user@example.com";
        String authHeader = "Bearer " + token;
        User user = UserFactory.create();

        AuthenticateUserCommand.Command command = AuthenticateUserCommand.Command.builder().authHeader(authHeader).build();

        when(jwtUtil.extractEmail(token)).thenReturn(email);
        when(getUserByEmailRepository.execute(email)).thenReturn(user);
        when(jwtUtil.validateToken(token, user)).thenReturn(true);

        AuthenticatedUser result = useCase.execute(command);

        assertNotNull(result);
        verify(jwtUtil).extractEmail(token);
        verify(getUserByEmailRepository).execute(email);
        verify(jwtUtil).validateToken(token, user);
    }

    @Test
    void GIVEN_invalid_auth_header_WHEN_execute_THEN_throw_unauthorized() {
        String authHeader = "InvalidHeader";
        AuthenticateUserCommand.Command command = AuthenticateUserCommand.Command.builder().authHeader(authHeader).build();

        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () -> useCase.execute(command));

        assertEquals(ErrorDescription.UNAUTHORIZED.getValue(), ex.getDescription());
    }

    @Test
    void GIVEN_token_with_null_email_WHEN_execute_THEN_throw_invalid_token() {
        String token = "token";
        String authHeader = "Bearer " + token;

        AuthenticateUserCommand.Command command = AuthenticateUserCommand.Command.builder().authHeader(authHeader).build();

        when(jwtUtil.extractEmail(token)).thenReturn(null);

        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () -> useCase.execute(command));
        assertEquals(ErrorDescription.INVALID_CREDENTIALS.getValue(), ex.getDescription());
    }

    @Test
    void GIVEN_invalid_user_or_token_WHEN_execute_THEN_throw_invalid_token() {
        String token = "token";
        String email = "user@example.com";
        String authHeader = "Bearer " + token;

        AuthenticateUserCommand.Command command = AuthenticateUserCommand.Command.builder().authHeader(authHeader).build();

        when(jwtUtil.extractEmail(token)).thenReturn(email);
        when(getUserByEmailRepository.execute(email)).thenReturn(null);

        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () -> useCase.execute(command));
        assertEquals(ErrorDescription.INVALID_CREDENTIALS.getValue(), ex.getDescription());
    }
}
