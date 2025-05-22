package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetIngredientsFromFileCommand;
import com.cuoco.application.usecase.model.Ingredient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetIngredientsFromFileUseCase implements GetIngredientsFromFileCommand {

    @Override
    public List<Ingredient> execute(Command command) {
        return List.of();
    }
}
