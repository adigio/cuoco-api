package com.cuoco.application.command;

import com.cuoco.application.port.in.AuthenticateUserCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AuthenticateUserCommand - 5 Unit Tests")
class AuthenticateUserCommandUnitTest {

    @Test
    @DisplayName("Test 1: Command should be created with valid JWT token")
    void test1_commandShouldBeCreatedWithValidJWTToken() {
        // Given
        String jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.validtoken";

        // When
        AuthenticateUserCommand.Command command = new AuthenticateUserCommand.Command(jwtToken);

        // Then
        assertNotNull(command);
        assertEquals(jwtToken, command.getAuthHeader());
    }

    @Test
    @DisplayName("Test 2: Command should handle empty token")
    void test2_commandShouldHandleEmptyToken() {
        // Given
        String emptyToken = "";

        // When
        AuthenticateUserCommand.Command command = new AuthenticateUserCommand.Command(emptyToken);

        // Then
        assertNotNull(command);
        assertEquals(emptyToken, command.getAuthHeader());
    }

    @Test
    @DisplayName("Test 3: Command should handle Bearer token format")
    void test3_commandShouldHandleBearerTokenFormat() {
        // Given
        String bearerToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.bearertoken";

        // When
        AuthenticateUserCommand.Command command = new AuthenticateUserCommand.Command(bearerToken);

        // Then
        assertNotNull(command);
        assertEquals(bearerToken, command.getAuthHeader());
        assertTrue(command.getAuthHeader().startsWith("Bearer "));
    }

    @Test
    @DisplayName("Test 4: Command should handle malformed token")
    void test4_commandShouldHandleMalformedToken() {
        // Given
        String malformedToken = "invalid.token.format";

        // When
        AuthenticateUserCommand.Command command = new AuthenticateUserCommand.Command(malformedToken);

        // Then
        assertNotNull(command);
        assertEquals(malformedToken, command.getAuthHeader());
    }

    @Test
    @DisplayName("Test 5: Command should handle null token")
    void test5_commandShouldHandleNullToken() {
        // Given
        String nullToken = null;

        // When
        AuthenticateUserCommand.Command command = new AuthenticateUserCommand.Command(nullToken);

        // Then
        assertNotNull(command);
        assertNull(command.getAuthHeader());
    }

    @Test
    @DisplayName("Test 6: Command should have proper toString method")
    void test6_commandShouldHaveProperToStringMethod() {
        // Given
        String authHeader = "Bearer jwt-token-123";

        // When
        AuthenticateUserCommand.Command command = new AuthenticateUserCommand.Command(authHeader);
        String result = command.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("Command"));
        assertTrue(result.contains("authHeader"));
    }
}