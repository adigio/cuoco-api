package com.cuoco.application.usecase.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

    @Getter
    @Setter
    @AllArgsConstructor
    public class User {

        private Integer id;

        private String name;

        private String email;

        private String password;

        private LocalDate registerDate;

        private String plan;

        private Boolean isValid;

        private String cookLevel;

        private String diet;
    }

