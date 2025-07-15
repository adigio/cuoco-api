package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.CalendarResponse;
import com.cuoco.adapter.in.controller.model.RecipeCalendarRequest;
import com.cuoco.adapter.in.controller.model.UserRecipeCalendarRequest;
import com.cuoco.application.port.in.CreateOrUpdateUserRecipeCalendarCommand;
import com.cuoco.application.port.in.GetUserCalendarQuery;
import com.cuoco.application.usecase.model.Calendar;
import com.cuoco.factory.domain.CalendarFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserCalendarControllerAdapterTest {

    @Mock
    private CreateOrUpdateUserRecipeCalendarCommand createOrUpdateUserRecipeCalendarCommand;

    @Mock
    private GetUserCalendarQuery getUserCalendarQuery;

    private UserCalendarControllerAdapter userCalendarControllerAdapter;

    @BeforeEach
    void setUp() {
        userCalendarControllerAdapter = new UserCalendarControllerAdapter(
                createOrUpdateUserRecipeCalendarCommand,
                getUserCalendarQuery
        );
    }

    @Test
    void shouldSaveCalendarSuccessfully() {
        List<UserRecipeCalendarRequest> requests = List.of(
                UserRecipeCalendarRequest.builder()
                        .dayId(1)
                        .recipes(List.of(RecipeCalendarRequest.builder()
                                .recipeId(1L)
                                .mealTypeId(1)
                                .build()))
                        .build()
        );

        ResponseEntity<?> response = userCalendarControllerAdapter.save(requests);

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
        verify(createOrUpdateUserRecipeCalendarCommand, times(1)).execute(any(CreateOrUpdateUserRecipeCalendarCommand.Command.class));
    }

    @Test
    void shouldGetCalendarSuccessfully() {
        List<Calendar> calendars = List.of(CalendarFactory.create());
        when(getUserCalendarQuery.execute()).thenReturn(calendars);

        ResponseEntity<List<CalendarResponse>> response = userCalendarControllerAdapter.get();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(getUserCalendarQuery, times(1)).execute();
    }
} 