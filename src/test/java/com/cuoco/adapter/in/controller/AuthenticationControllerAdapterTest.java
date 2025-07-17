package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.AuthRequest;
import com.cuoco.adapter.in.controller.model.UserRequest;
import com.cuoco.application.port.in.AuthenticateUserCommand;
import com.cuoco.application.port.in.CreateUserCommand;
import com.cuoco.application.port.in.SignInUserCommand;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.AuthenticatedUser;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.User;
import com.cuoco.factory.domain.UserFactory;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerAdapterTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private SignInUserCommand signInUserCommand;

    @Mock
    private CreateUserCommand createUserCommand;

    @Mock
    private AuthenticateUserCommand authenticateUserCommand;

    @InjectMocks
    private AuthenticationControllerAdapter authenticationControllerAdapter;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationControllerAdapter).build();
    }

    @Test
    void GIVEN_valid_credentials_WHEN_login_THEN_return_auth_response() throws Exception {
        User user = UserFactory.create();

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                user,
                "token123",
                List.of()
        );

        AuthRequest request = AuthRequest.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        when(signInUserCommand.execute(any())).thenReturn(authenticatedUser);

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.user.name").value(user.getName()))
                .andExpect(jsonPath("$.data.user.email").value(user.getEmail()))
                .andExpect(jsonPath("$.data.user.token").value(authenticatedUser.getToken()))
                .andExpect(jsonPath("$.data.user.plan.description").value(user.getPlan().getDescription()));
    }

    @Test
    void GIVEN_valid_user_data_WHEN_register_THEN_return_created_user_response() throws Exception {
        User user = UserFactory.create();

        UserRequest request = UserRequest.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .planId(user.getPlan().getId())
                .cookLevelId(user.getPreferences().getCookLevel().getId())
                .dietId(user.getPreferences().getDiet().getId())
                .dietaryNeeds(user.getDietaryNeeds().stream().map(DietaryNeed::getId).toList())
                .allergies(user.getAllergies().stream().map(Allergy::getId).toList())
                .build();

        when(createUserCommand.execute(any())).thenReturn(user);

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.plan.description").value(user.getPlan().getDescription()));

    }
}