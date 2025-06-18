package com.cuoco.adapter.in.security;

import com.cuoco.application.port.in.AuthenticateUserCommand;
import com.cuoco.application.usecase.model.AuthenticatedUser;
import com.cuoco.application.usecase.model.User;
import com.cuoco.factory.domain.UserFactory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterAdapterTest {

    @Mock
    private AuthenticateUserCommand authenticateUserCommand;

    @Mock
    private FilterChain filterChain;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private JwtAuthenticationFilterAdapter filter;

    @BeforeEach
    void setUp() {
        filter = new JwtAuthenticationFilterAdapter(authenticateUserCommand);
    }

    @Test
    void GIVEN_valid_authorization_header_WHEN_doFilterInternal_THEN_authentication_is_set() throws Exception {
        String token = "Bearer valid.jwt.token";
        User user = UserFactory.create();
        List<String> roles = List.of("ROLE_USER");

        AuthenticatedUser authenticatedUser = AuthenticatedUser.builder()
                .user(user)
                .roles(roles)
                .build();

        when(request.getHeader("Authorization")).thenReturn(token);
        when(authenticateUserCommand.execute(any())).thenReturn(authenticatedUser);

        filter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals(user.getEmail(), auth.getPrincipal());
        assertEquals(1, auth.getAuthorities().size());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void GIVEN_null_authentication_WHEN_doFilterInternal_THEN_authentication_is_not_set() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid.token");
        when(authenticateUserCommand.execute(any())).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/auth/login",
            "/auth/register",
            "/actuator/health",
            "/plans",
            "/allergies",
            "/diets",
            "/dietary-needs",
            "/cook-levels",
            "/v3/api-docs/test",
            "/swagger-ui/index.html",
            "/swagger-ui.html"
    })
    void GIVEN_excluded_paths_WHEN_shouldNotFilter_THEN_returns_true(String path) {
        when(request.getRequestURI()).thenReturn(path);
        assertTrue(filter.shouldNotFilter(request));
    }

    @Test
    void GIVEN_non_excluded_path_WHEN_shouldNotFilter_THEN_returns_false() {
        when(request.getRequestURI()).thenReturn("/secure-endpoint");
        assertFalse(filter.shouldNotFilter(request));
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }
}
