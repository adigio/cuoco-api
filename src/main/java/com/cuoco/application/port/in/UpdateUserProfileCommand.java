package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public interface UpdateUserProfileCommand {

    User execute(Command command);

    @Data
    @Builder
    @AllArgsConstructor
    class Command {
        private final String name;
        private final Integer planId;
        private final Integer cookLevelId;
        private final Integer dietId;
        private final List<Integer> dietaryNeeds;
        private final List<Integer> allergies;
    }
}