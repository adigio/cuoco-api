package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.DietaryNeeds;
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

        private LocalDate registerDate;

        private String plan;

        private Boolean isValid;

        private String cookLevel;

        private String diet;

        private List<String> dietaryNeeds;

    }
}