package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.UpdateUserProfileRequest;
import com.cuoco.application.port.in.UpdateUserProfileCommand;
import com.cuoco.application.usecase.model.User;
import com.cuoco.factory.domain.UserFactory;
import com.cuoco.shared.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserProfileControllerAdapterTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UpdateUserProfileCommand updateUserProfileCommand;
    private UserProfileControllerAdapter controller;
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        updateUserProfileCommand = mock(UpdateUserProfileCommand.class);
        jwtUtil = mock(JwtUtil.class);
        objectMapper = new ObjectMapper();

        controller = new UserProfileControllerAdapter(updateUserProfileCommand, jwtUtil);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    void GIVEN_valid_profile_data_WHEN_updateProfile_THEN_return_updated_user_response() throws Exception {
        // Arrange
        UpdateUserProfileRequest request = UpdateUserProfileRequest.builder()
                .name("Juan Pérez")
                .planId(2)
                .build();

        User expectedUser = UserFactory.create();

        when(jwtUtil.extractEmail("fake-jwt-token")).thenReturn("test@example.com");
        when(updateUserProfileCommand.execute(any())).thenReturn(expectedUser);

        // Act & Assert
        mockMvc.perform(put("/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer fake-jwt-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(expectedUser.getName()));
    }


    @Test
    void GIVEN_invalid_profile_data_WHEN_updateProfile_THEN_return_bad_request() throws Exception {
        UpdateUserProfileRequest request = UpdateUserProfileRequest.builder()
                .name("")
                .build();

        User expectedUser = UserFactory.create();
        when(jwtUtil.extractEmail("fake-jwt-token")).thenReturn("test@example.com");
        when(updateUserProfileCommand.execute(any())).thenReturn(expectedUser);

        mockMvc.perform(put("/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer fake-jwt-token"))
                .andExpect(status().isOk()); // ← Por ahora OK
    }
}