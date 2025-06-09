package com.cuoco.application.usecase;

import com.cuoco.application.port.in.AuthenticateUserCommand;
import com.cuoco.application.port.out.GetUserByEmailRepository;
import com.cuoco.application.usecase.model.AuthenticatedUser;
import com.cuoco.application.usecase.model.User;
import com.cuoco.shared.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticateUserUseCase - Unit Tests")
class AuthenticateUserUseCaseUnitTest {

    @Mock
    private GetUserByEmailRepository getUserByEmailRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private SecurityContext securityContext;

    private AuthenticateUserUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AuthenticateUserUseCase(jwtUtil, getUserByEmailRepository);
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Test 1: Should authenticate successfully with valid Bearer token")
    void test1_shouldAuthenticateSuccessfullyWithValidBearerToken() {
        // Given
        String authHeader = "Bearer valid-jwt-token-123";
        String email = "john@example.com";
        AuthenticateUserCommand.Command command = new AuthenticateUserCommand.Command(authHeader);

        User expectedUser = new User(1L, "John", email, "password", null, true, null, LocalDateTime.now(), Collections.emptyList(), Collections.emptyList());

        when(jwtUtil.extractEmail("valid-jwt-token-123")).thenReturn(email);
        when(getUserByEmailRepository.execute(email)).thenReturn(expectedUser);
        when(jwtUtil.validateToken("valid-jwt-token-123", expectedUser)).thenReturn(true);

        try (MockedStatic<SecurityContextHolder> securityContextHolderMock = mockStatic(SecurityContextHolder.class)) {
            securityContextHolderMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(null);

            // When
            AuthenticatedUser result = useCase.execute(command);

            // Then
            assertNotNull(result);
            assertNotNull(result.getUser());
            assertEquals(email, result.getUser().getEmail());
            assertEquals("John", result.getUser().getName());
            assertNull(result.getToken());
            assertTrue(result.getRoles().isEmpty());

            verify(jwtUtil, times(1)).extractEmail("valid-jwt-token-123");
            verify(getUserByEmailRepository, times(1)).execute(email);
            verify(jwtUtil, times(1)).validateToken("valid-jwt-token-123", expectedUser);
        }
    }

    @Test
    @DisplayName("Test 2: Should return null when auth header is null or doesn't start with Bearer")
    void test2_shouldReturnNullWhenAuthHeaderIsInvalid() {
        // Given - null header
        AuthenticateUserCommand.Command commandNull = new AuthenticateUserCommand.Command(null);

        // When
        AuthenticatedUser resultNull = useCase.execute(commandNull);

        // Then
        assertNull(resultNull);

        // Given - invalid prefix
        AuthenticateUserCommand.Command commandInvalid = new AuthenticateUserCommand.Command("InvalidPrefix token");

        // When
        AuthenticatedUser resultInvalid = useCase.execute(commandInvalid);

        // Then
        assertNull(resultInvalid);

        // Verify no interactions with mocked dependencies
        verifyNoInteractions(jwtUtil);
        verifyNoInteractions(getUserByEmailRepository);
    }

    @Test
    @DisplayName("Test 3: Should return null when JWT email extraction fails")
    void test3_shouldReturnNullWhenJwtEmailExtractionFails() {
        // Given
        String authHeader = "Bearer invalid-jwt-token";
        AuthenticateUserCommand.Command command = new AuthenticateUserCommand.Command(authHeader);

        when(jwtUtil.extractEmail("invalid-jwt-token")).thenReturn(null);

        // When
        AuthenticatedUser result = useCase.execute(command);

        // Then
        assertNull(result);

        verify(jwtUtil, times(1)).extractEmail("invalid-jwt-token");
        verifyNoInteractions(getUserByEmailRepository);
    }

    @Test
    @DisplayName("Test 4: Should return null when SecurityContext already has authentication")
    void test4_shouldReturnNullWhenSecurityContextAlreadyHasAuthentication() {
        // Given
        String authHeader = "Bearer valid-jwt-token-123";
        String email = "john@example.com";
        AuthenticateUserCommand.Command command = new AuthenticateUserCommand.Command(authHeader);

        when(jwtUtil.extractEmail("valid-jwt-token-123")).thenReturn(email);

        try (MockedStatic<SecurityContextHolder> securityContextHolderMock = mockStatic(SecurityContextHolder.class)) {
            securityContextHolderMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(mock(org.springframework.security.core.Authentication.class));

            // When
            AuthenticatedUser result = useCase.execute(command);

            // Then
            assertNull(result);

            verify(jwtUtil, times(1)).extractEmail("valid-jwt-token-123");
            verifyNoInteractions(getUserByEmailRepository);
        }
    }

    @Test
    @DisplayName("Test 5: Should return null when user is not found")
    void test5_shouldReturnNullWhenUserIsNotFound() {
        // Given
        String authHeader = "Bearer valid-jwt-token-123";
        String email = "nonexistent@example.com";
        AuthenticateUserCommand.Command command = new AuthenticateUserCommand.Command(authHeader);

        when(jwtUtil.extractEmail("valid-jwt-token-123")).thenReturn(email);
        when(getUserByEmailRepository.execute(email)).thenReturn(null);

        try (MockedStatic<SecurityContextHolder> securityContextHolderMock = mockStatic(SecurityContextHolder.class)) {
            securityContextHolderMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(null);

            // When
            AuthenticatedUser result = useCase.execute(command);

            // Then
            assertNull(result);

            verify(jwtUtil, times(1)).extractEmail("valid-jwt-token-123");
            verify(getUserByEmailRepository, times(1)).execute(email);
            verify(jwtUtil, never()).validateToken(any(), any());
        }
    }

    @Test
    @DisplayName("Test 6: Should return null when token validation fails")
    void test6_shouldReturnNullWhenTokenValidationFails() {
        // Given
        String authHeader = "Bearer invalid-jwt-token-123";
        String email = "john@example.com";
        AuthenticateUserCommand.Command command = new AuthenticateUserCommand.Command(authHeader);

        User user = new User(1L, "John", email, "password", null, true, null, LocalDateTime.now(), Collections.emptyList(), Collections.emptyList());

        when(jwtUtil.extractEmail("invalid-jwt-token-123")).thenReturn(email);
        when(getUserByEmailRepository.execute(email)).thenReturn(user);
        when(jwtUtil.validateToken("invalid-jwt-token-123", user)).thenReturn(false);

        try (MockedStatic<SecurityContextHolder> securityContextHolderMock = mockStatic(SecurityContextHolder.class)) {
            securityContextHolderMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(null);

            // When
            AuthenticatedUser result = useCase.execute(command);

            // Then
            assertNull(result);

            verify(jwtUtil, times(1)).extractEmail("invalid-jwt-token-123");
            verify(getUserByEmailRepository, times(1)).execute(email);
            verify(jwtUtil, times(1)).validateToken("invalid-jwt-token-123", user);
        }
    }
}