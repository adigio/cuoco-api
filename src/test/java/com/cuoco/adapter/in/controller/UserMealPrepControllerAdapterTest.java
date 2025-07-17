package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.MealPrepResponse;
import com.cuoco.application.port.in.CreateUserMealPrepCommand;
import com.cuoco.application.port.in.DeleteUserMealPrepCommand;
import com.cuoco.application.port.in.GetAllUserMealPrepsQuery;
import com.cuoco.application.usecase.model.MealPrep;
import com.cuoco.factory.domain.MealPrepFactory;
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
class UserMealPrepControllerAdapterTest {

    @Mock
    private CreateUserMealPrepCommand createUserMealPrepCommand;

    @Mock
    private GetAllUserMealPrepsQuery getAllUserMealPrepsQuery;

    @Mock
    private DeleteUserMealPrepCommand deleteUserMealPrepCommand;

    private UserMealPrepControllerAdapter userMealPrepControllerAdapter;

    @BeforeEach
    void setUp() {
        userMealPrepControllerAdapter = new UserMealPrepControllerAdapter(
                createUserMealPrepCommand,
                getAllUserMealPrepsQuery,
                deleteUserMealPrepCommand
        );
    }

    @Test
    void shouldSaveMealPrepSuccessfully() {
        Long mealPrepId = 1L;

        ResponseEntity<?> response = userMealPrepControllerAdapter.save(mealPrepId);

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
        verify(createUserMealPrepCommand, times(1)).execute(any(CreateUserMealPrepCommand.Command.class));
    }

    @Test
    void shouldGetAllMealPrepsSuccessfully() {
        List<MealPrep> mealPreps = List.of(MealPrepFactory.create());
        when(getAllUserMealPrepsQuery.execute()).thenReturn(mealPreps);

        ResponseEntity<List<MealPrepResponse>> response = userMealPrepControllerAdapter.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(getAllUserMealPrepsQuery, times(1)).execute();
    }

    @Test
    void shouldDeleteMealPrepSuccessfully() {
        Long mealPrepId = 1L;

        ResponseEntity<?> response = userMealPrepControllerAdapter.delete(mealPrepId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(deleteUserMealPrepCommand, times(1)).execute(any(DeleteUserMealPrepCommand.Command.class));
    }
} 