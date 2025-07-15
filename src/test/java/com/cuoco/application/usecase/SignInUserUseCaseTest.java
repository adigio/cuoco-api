package com.cuoco.application.usecase;

import com.cuoco.application.exception.ForbiddenException;
import com.cuoco.application.port.in.SignInUserCommand;
import com.cuoco.application.port.out.GetUserByEmailRepository;
import com.cuoco.application.usecase.model.AuthenticatedUser;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.utils.JwtUtil;
import com.cuoco.shared.model.ErrorDescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SignInUserUseCaseTest {

    private GetUserByEmailRepository getUserByEmailRepository;
    private JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;

    private SignInUserUseCase useCase;

    @BeforeEach
    void setup() {
        getUserByEmailRepository = mock(GetUserByEmailRepository.class);
        jwtUtil = mock(JwtUtil.class);
        passwordEncoder = mock(PasswordEncoder.class);

        useCase = new SignInUserUseCase(getUserByEmailRepository, jwtUtil, passwordEncoder);
    }

    @Test
    void GIVEN_valid_credentials_WHEN_execute_THEN_return_authenticated_user() {
        String email = "test@example.com";
        String rawPassword = "password123";
        String encodedPassword = "$2a$10$encodedPasswordHash";

        User user = User.builder()
                .email(email)
                .password(encodedPassword)
                .build();

        when(getUserByEmailRepository.execute(email)).thenReturn(user);
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        when(jwtUtil.generateToken(any(User.class))).thenReturn("jwt-token");

        SignInUserCommand.Command command = SignInUserCommand.Command.builder()
                .email(email)
                .password(rawPassword)
                .build();

        AuthenticatedUser result = useCase.execute(command);

        assertNotNull(result);
        assertEquals("jwt-token", result.getToken());
        assertEquals(email, result.getUser().getEmail());
        assertNull(result.getUser().getPassword());
    }

    @Test
    void GIVEN_invalid_password_WHEN_execute_THEN_throw_forbidden_exception() {
        String email = "test@example.com";
        String rawPassword = "wrong-password";
        String encodedPassword = "$2a$10$encodedPasswordHash";

        User user = User.builder()
                .email(email)
                .password(encodedPassword)
                .build();

        when(getUserByEmailRepository.execute(email)).thenReturn(user);
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        SignInUserCommand.Command command = SignInUserCommand.Command.builder()
                .email(email)
                .password(rawPassword)
                .build();

        ForbiddenException ex = assertThrows(ForbiddenException.class, () -> useCase.execute(command));
        assertEquals(ErrorDescription.INVALID_CREDENTIALS.getValue(), ex.getDescription());
    }
}
