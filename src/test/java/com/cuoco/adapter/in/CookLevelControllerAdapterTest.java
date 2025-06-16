package com.cuoco.adapter.in;

import com.cuoco.adapter.in.controller.AllergyControllerAdapter;
import com.cuoco.adapter.in.controller.CookLevelControllerAdapter;
import com.cuoco.application.port.in.AuthenticateUserCommand;
import com.cuoco.application.port.in.GetAllAllergiesQuery;
import com.cuoco.application.port.in.GetAllCookLevelsQuery;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.CookLevel;
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

@WebMvcTest(CookLevelControllerAdapter.class)
public class CookLevelControllerAdapterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetAllCookLevelsQuery getAllCookLevelsQuery;

    @MockitoBean
    private AuthenticateUserCommand authenticateUserCommand;

    @Test
    @WithMockUser
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
