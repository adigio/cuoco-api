package com.cuoco.application.controller;

import com.cuoco.adapter.in.controller.AuthenticationControllerAdapter;
import com.cuoco.adapter.in.controller.model.AuthRequest;
import com.cuoco.adapter.in.controller.model.AuthResponse;
import com.cuoco.adapter.in.controller.model.UserRequest;
import com.cuoco.application.port.in.CreateUserCommand;
import com.cuoco.application.port.in.SignInUserCommand;
import com.cuoco.application.usecase.model.AuthenticatedUser;
import com.cuoco.application.usecase.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticationControllerAdapter - Unit Tests")
class AuthenticationControllerAdapterUnitTest {

    @Mock
    private CreateUserCommand createUserCommand;

    @Mock
    private SignInUserCommand signInUserCommand;

    private AuthenticationControllerAdapter controller;

    @BeforeEach
    void setUp() {
        controller = new AuthenticationControllerAdapter(signInUserCommand, createUserCommand);
    }

    @Test
    @DisplayName("Test 1: Successful login returns token")
    void test1_loginSuccess() {
        AuthRequest request = new AuthRequest();
        request.setEmail("john@example.com");
        request.setPassword("password123");

        User user = new User(1L, "John", "john@example.com", null, null, true, null, LocalDateTime.now(), Collections.emptyList(), Collections.emptyList());
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(user, "jwt-token", Arrays.asList("USER"));

        when(signInUserCommand.execute(any())).thenReturn(authenticatedUser);

        ResponseEntity<?> response = controller.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof AuthResponse);
        assertEquals("jwt-token", ((AuthResponse) response.getBody()).getToken());
    }


    @Test
    @DisplayName("Test 2: Successful registration returns 201 CREATED")
    void test2_registerSuccess() {
        UserRequest request = new UserRequest(
                "Jane",
                "securepass",
                "jane@example.com",
                "Free",
                "Intermediate",
                "Vegan",
                Arrays.asList("High Protein"),
                Arrays.asList("Peanuts", "Shellfish")
        );

        ResponseEntity<?> response = controller.register(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(createUserCommand, times(1)).execute(any());
    }

    @Test
    @DisplayName("Test 3: Login throws exception when credentials are invalid")
    void test3_loginThrowsException() {
        AuthRequest request = new AuthRequest();
        request.setEmail("invalid@example.com");
        request.setPassword("wrong");

        when(signInUserCommand.execute(any())).thenThrow(new RuntimeException("Invalid credentials"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> controller.login(request));
        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    @DisplayName("Test 4: Register throws exception on validation failure")
    void test4_registerThrowsException() {
        UserRequest request = new UserRequest(
                "InvalidUser",
                "123",
                "bad-email",
                "Free",
                "Beginner",
                "None",
                Collections.emptyList(),
                Collections.emptyList()
        );

        doThrow(new RuntimeException("Validation error")).when(createUserCommand).execute(any());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> controller.register(request));
        assertEquals("Validation error", exception.getMessage());
    }


    @Test
    @DisplayName("Test 5: Login with null email throws exception")
    void test5_loginNullEmail() {
        AuthRequest request = new AuthRequest();
        request.setEmail(null);
        request.setPassword("pass");

        when(signInUserCommand.execute(any())).thenThrow(new NullPointerException("Email is null"));

        assertThrows(NullPointerException.class, () -> controller.login(request));
    }

    @Test
    @DisplayName("Test 6: Register with missing required fields")
    void test6_registerMissingFields() {
        UserRequest request = new UserRequest(
                "",                   // name vacío
                "",                   // password vacío
                "",                   // email vacío
                "",                   // plan vacío
                "",                   // cookLevel vacío
                "",                   // diet vacío
                null,                 // dietaryNeeds nulo
                null                  // allergies nulo
        );

        doThrow(new IllegalArgumentException("Missing fields")).when(createUserCommand).execute(any());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> controller.register(request));
        assertEquals("Missing fields", ex.getMessage());
    }
}
