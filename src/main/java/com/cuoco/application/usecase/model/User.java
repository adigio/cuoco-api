package com.cuoco.application.usecase.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

    @Getter
    @Setter
    @Table(name = "usuario", schema = "cuoco")
    public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        private Integer id;

        @Column(name = "nombre", nullable = false, length = 45)
        private String nombre;

        @Column(name = "email", nullable = false, length = 45)
        private String email;

        @Column(name = "password", nullable = false, length = 45)
        private String password;

        @Column(name = "fechaRegistro", nullable = false)
        private LocalDate fechaRegistro;

        @Column(name = "Plan", nullable = false, length = 20)
        private String plan;

        @Column(name = "isValid", nullable = false)
        private Byte isValid;

        @Lob
        @Column(name = "nivelCocina")
        private String nivelCocina;


        public User(Object o, Object o1, Object o2, String nombre, String encriptedPassword) {
        }
    }

