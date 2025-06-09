package com.cuoco.application.command;

import com.cuoco.application.port.in.SignInUserCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SignInUserCommand - 5 Unit Tests")
class SignInUserCommandUnitTest {

    @Test
    @DisplayName("Test 1: Command should be created with valid credentials")
    void test1_validCredentials() {
        // Given
        String email = "john@example.com";
        String password = "password123";

        // When
        SignInUserCommand.Command command = new SignInUserCommand.Command(email, password);

        // Then
        assertNotNull(command);
        assertEquals(email, command.getEmail());
        assertEquals(password, command.getPassword());
    }

    @Test
    @DisplayName("Test 2: Command should handle empty email")
    void test2_emptyEmail() {
        // Given
        String email = "";
        String password = "password123";

        // When
        SignInUserCommand.Command command = new SignInUserCommand.Command(email, password);

        // Then
        assertEquals("", command.getEmail());
        assertEquals("password123", command.getPassword());
    }

    @Test
    @DisplayName("Test 3: Command should handle null email")
    void test3_nullEmail() {
        // Given
        String email = null;
        String password = "password123";

        // When
        SignInUserCommand.Command command = new SignInUserCommand.Command(email, password);

        // Then
        assertNull(command.getEmail());
        assertEquals("password123", command.getPassword());
    }

    @Test
    @DisplayName("Test 4: Command should handle special characters in password")
    void test4_specialCharactersInPassword() {
        // Given
        String email = "special@domain.com";
        String password = "P@ssw0rd!@#";

        // When
        SignInUserCommand.Command command = new SignInUserCommand.Command(email, password);

        // Then
        assertEquals("special@domain.com", command.getEmail());
        assertEquals("P@ssw0rd!@#", command.getPassword());
    }

    @Test
    @DisplayName("Test 5: Command should handle null password")
    void test5_nullPassword() {
        // Given
        String email = "user@example.com";
        String password = null;

        // When
        SignInUserCommand.Command command = new SignInUserCommand.Command(email, password);

        // Then
        assertEquals("user@example.com", command.getEmail());
        assertNull(command.getPassword());
    }
}
