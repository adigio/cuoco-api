package com.cuoco.adapter.in.controller;

import com.cuoco.application.port.in.AuthenticateUserCommand;
import com.cuoco.application.port.in.GetAllCookLevelsQuery;
import com.cuoco.application.usecase.model.CookLevel;
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
public class CookLevelControllerAdapterTest {

    private MockMvc mockMvc;

    @Mock
    private GetAllCookLevelsQuery getAllCookLevelsQuery;

    @Mock
    private AuthenticateUserCommand authenticateUserCommand;

    @InjectMocks
    private CookLevelControllerAdapter cookLevelControllerAdapter;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cookLevelControllerAdapter).build();
    }

    @Test
    void GIVEN_existing_cook_levels_WHEN_getAll_THEN_return_list_of_parametric_response() throws Exception {
        List<CookLevel> cookLevels = List.of(
                CookLevel.builder().id(1).description("Level 1").build(),
                CookLevel.builder().id(2).description("Level 2").build()
        );

        when(getAllCookLevelsQuery.execute()).thenReturn(cookLevels);

        mockMvc.perform(get("/cook-levels").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Level 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].description").value("Level 2"));
    }
}
