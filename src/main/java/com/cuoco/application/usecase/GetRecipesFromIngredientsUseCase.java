package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetRecipesFromIngredientsCommand;
import org.springframework.stereotype.Component;

@Component
public class GetRecipesFromIngredientsUseCase implements GetRecipesFromIngredientsCommand {

    public String execute(Command command) {
        return "";
    }

}
