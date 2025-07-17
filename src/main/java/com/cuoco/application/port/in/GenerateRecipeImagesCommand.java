package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.Step;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public interface GenerateRecipeImagesCommand {

    List<Step> execute(Command command);

    @Data
    @Builder
    class Command {
        private final Recipe recipe;
    }
} 