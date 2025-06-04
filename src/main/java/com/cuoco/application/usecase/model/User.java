package com.cuoco.application.usecase.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
    @Setter
    @AllArgsConstructor
    public class User {

        private Long id;

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

