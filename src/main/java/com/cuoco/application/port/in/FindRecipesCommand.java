package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.Recipe;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public interface FindRecipesCommand {

    List<Recipe> execute(Command command);

    @Data
    @Builder
    class Command {
        private Integer size;
        private Boolean random;
    }
}
