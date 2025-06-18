package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetIngredientsFromTextCommand;
import com.cuoco.application.port.out.GetIngredientsFromTextRepository;
import com.cuoco.application.usecase.model.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetIngredientsFromTextUseCaseTest {

    private GetIngredientsFromTextRepository getIngredientsFromTextRepository;
    private GetIngredientsFromTextUseCase useCase;

    @BeforeEach
    void setup() {
        getIngredientsFromTextRepository = mock(GetIngredientsFromTextRepository.class);
        useCase = new GetIngredientsFromTextUseCase(getIngredientsFromTextRepository);
    }

    @Test
    void GIVEN_valid_text_WHEN_execute_THEN_return_ingredient_list() {
        String text = "tomate, cebolla";
        List<Ingredient> expectedIngredients = List.of(
                Ingredient.builder().name("tomate").build(),
                Ingredient.builder().name("cebolla").build()
        );

        when(getIngredientsFromTextRepository.execute(text)).thenReturn(expectedIngredients);

        GetIngredientsFromTextCommand.Command command = GetIngredientsFromTextCommand.Command.builder()
                .text(text)
                .build();

        List<Ingredient> result = useCase.execute(command);

        assertEquals(2, result.size());
        assertEquals("tomate", result.get(0).getName());
        assertEquals("cebolla", result.get(1).getName());
        verify(getIngredientsFromTextRepository, times(1)).execute(text);
    }
}
