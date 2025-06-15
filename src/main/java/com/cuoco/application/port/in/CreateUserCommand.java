package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

public interface CreateUserCommand {

    User execute(Command command);

    @Data
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