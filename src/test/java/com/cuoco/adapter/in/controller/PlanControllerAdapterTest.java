package com.cuoco.adapter.in.controller;


import com.cuoco.application.port.in.AuthenticateUserCommand;
import com.cuoco.application.port.in.GetAllPlansQuery;
import com.cuoco.application.usecase.model.Plan;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlanControllerAdapter.class)
class PlanControllerAdapterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetAllPlansQuery getAllPlansQuery;

    @MockitoBean
    private AuthenticateUserCommand authenticateUserCommand;

    @Test
    @WithMockUser
    void GIVEN_existing_plans_WHEN_getAll_THEN_return_list_of_parametric_response() throws Exception {
        List<Plan> plans = List.of(
                Plan.builder().id(1).description("Free").build(),
                Plan.builder().id(2).description("Pro").build()
        );

        when(getAllPlansQuery.execute()).thenReturn(plans);

        mockMvc.perform(get("/plans").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Free"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].description").value("Pro"));
    }
}
