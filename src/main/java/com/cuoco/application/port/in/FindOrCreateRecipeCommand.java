package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.Recipe;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

public interface FindOrCreateRecipeCommand {

    Recipe execute(Command command);

    @Data
    @Builder
    @ToString
    class Command {
        private String recipeName;
    }
} 