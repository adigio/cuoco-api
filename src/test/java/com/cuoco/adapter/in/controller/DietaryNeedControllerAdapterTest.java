package com.cuoco.adapter.in.controller;

import com.cuoco.application.port.in.AuthenticateUserCommand;
import com.cuoco.application.port.in.GetAllDietaryNeedsQuery;
import com.cuoco.application.usecase.model.DietaryNeed;
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
public class DietaryNeedControllerAdapterTest {

    private MockMvc mockMvc;

    @Mock
    private GetAllDietaryNeedsQuery getAllDietaryNeedsQuery;

    @Mock
    private AuthenticateUserCommand authenticateUserCommand;

    @InjectMocks
    private DietaryNeedControllerAdapter dietaryNeedControllerAdapter;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dietaryNeedControllerAdapter).build();
    }

    @Test
    void GIVEN_existing_dietary_needs_WHEN_getAll_THEN_return_list_of_parametric_response() throws Exception {
        List<DietaryNeed> dietaryNeeds = List.of(
                DietaryNeed.builder().id(1).description("Need 1").build(),
                DietaryNeed.builder().id(2).description("Need 2").build()
        );

        when(getAllDietaryNeedsQuery.execute()).thenReturn(dietaryNeeds);

        mockMvc.perform(get("/dietary-needs").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Need 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].description").value("Need 2"));
    }

}
