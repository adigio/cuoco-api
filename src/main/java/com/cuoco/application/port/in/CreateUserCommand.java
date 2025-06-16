package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

public interface CreateUserCommand {

    User execute(Command command);

    @Data
    @Builder
    @ToString
    @AllArgsConstructor
    class Command {

        private String name;
        private String email;
        private String password;
        private Integer planId;
        private Integer cookLevelId;
        private Integer dietId;

        private List<Integer> dietaryNeeds;
        private List<Integer> allergies;
    }
}