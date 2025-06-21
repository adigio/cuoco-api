package com.cuoco.adapter.in.controller;

import com.cuoco.application.port.in.SaveUserRecipeCommand;
import com.cuoco.application.usecase.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserRecipeControllerAdapterTest {

    private SaveUserRecipeCommand saveUserRecipeCommand;
    private UserRecipeControllerAdapter userRecipeControllerAdapter;

    @BeforeEach
    public void setUp() {
        saveUserRecipeCommand = mock(SaveUserRecipeCommand.class);
        userRecipeControllerAdapter = new UserRecipeControllerAdapter(saveUserRecipeCommand);
    }

    @Test
    public void saveRecipe_shouldReturnOk_whenSavedSuccessfully() throws Exception {
        // Arrange
        User user = new User();
        user.setName("testUser");
        setAuthentication(user);
        when(saveUserRecipeCommand.execute(any(SaveUserRecipeCommand.Command.class))).thenReturn(true);

        // Act
        ResponseEntity<?> response = userRecipeControllerAdapter.save(123L);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(true, response.getBody());
    }

    // Utilidad para setear un usuario autenticado en el contexto de seguridad
    private void setAuthentication(User user) {
        TestingAuthenticationToken auth = new TestingAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
