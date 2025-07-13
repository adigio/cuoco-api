package com.cuoco.adapter.in.controller;

import com.cuoco.application.port.in.AuthenticateUserCommand;
import com.cuoco.application.port.in.GetAllDietsQuery;
import com.cuoco.application.usecase.model.Diet;
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
public class DietControllerAdapterTest {

    private MockMvc mockMvc;

    @Mock
    private GetAllDietsQuery getAllDietsQuery;

    @Mock
    private AuthenticateUserCommand authenticateUserCommand;

    @InjectMocks
    private DietControllerAdapter dietControllerAdapter;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dietControllerAdapter).build();
    }

    @Test
    void GIVEN_existing_diets_WHEN_getAll_THEN_return_list_of_parametric_response() throws Exception {
        List<Diet> diets = List.of(
                Diet.builder().id(1).description("Diet 1").build(),
                Diet.builder().id(2).description("Diet 2").build()
        );

        when(getAllDietsQuery.execute()).thenReturn(diets);

        mockMvc.perform(get("/diets").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Diet 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].description").value("Diet 2"));
    }

}
