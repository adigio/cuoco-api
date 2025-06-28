package com.cuoco.application.port.in;

import lombok.Builder;
import lombok.Data;

public interface DeleteUserRecipeCommand {
    void execute(Command command);

    @Data
    @Builder
    class Command {
        private Long recipeId;
    }
}
