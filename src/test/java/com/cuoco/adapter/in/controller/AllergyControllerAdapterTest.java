package com.cuoco.adapter.in.controller;

import com.cuoco.application.port.in.AuthenticateUserCommand;
import com.cuoco.application.port.in.GetAllAllergiesQuery;
import com.cuoco.application.usecase.model.Allergy;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AllergyControllerAdapterTest {

    private MockMvc mockMvc;

    @Mock
    private GetAllAllergiesQuery getAllAllergiesQuery;

    @Mock
    private AuthenticateUserCommand authenticateUserCommand;

    @InjectMocks
    private AllergyControllerAdapter allergyControllerAdapter;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(allergyControllerAdapter).build();
    }

    @Test
    void GIVEN_existing_allergies_WHEN_getAll_THEN_return_list_of_parametric_response() throws Exception {
        List<Allergy> allergies = List.of(
                Allergy.builder().id(1).description("Mani").build(),
                Allergy.builder().id(2).description("Almeja").build()
        );

        when(getAllAllergiesQuery.execute()).thenReturn(allergies);

        mockMvc.perform(get("/allergies").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Mani"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].description").value("Almeja"));
    }
}

