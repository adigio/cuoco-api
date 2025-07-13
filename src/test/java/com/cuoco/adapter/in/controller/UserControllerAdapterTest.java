package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.UpdateUserRequest;
import com.cuoco.application.port.in.UpdateUserProfileCommand;
import com.cuoco.application.usecase.model.User;
import com.cuoco.factory.domain.UserFactory;
import com.cuoco.application.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerAdapterTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private UpdateUserProfileCommand updateUserProfileCommand;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserControllerAdapter controller;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void GIVEN_valid_profile_data_WHEN_updateProfile_THEN_return_updated_user_response() throws Exception {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .name("Juan Pérez")
                .planId(2)
                .build();

        User expectedUser = UserFactory.create();

        when(updateUserProfileCommand.execute(any())).thenReturn(expectedUser);

        mockMvc.perform(patch("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(expectedUser.getName()))
                .andExpect(jsonPath("$.email").value(expectedUser.getEmail()))
                .andExpect(jsonPath("$.id").value(expectedUser.getId()));
    }

    @Test
    void GIVEN_invalid_profile_data_WHEN_updateProfile_THEN_return_bad_request() throws Exception {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .name("")
                .build();

        User expectedUser = UserFactory.create();
        when(updateUserProfileCommand.execute(any())).thenReturn(expectedUser);

        mockMvc.perform(patch("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}