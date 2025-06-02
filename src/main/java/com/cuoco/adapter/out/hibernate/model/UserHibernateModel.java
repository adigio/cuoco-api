package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user")
public class UserHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer  id;
    private String name;
    private String email;
    private String password;
    private LocalDate registerDate;
    private String plan;
    private Boolean isValid;
    private String cookLevel;
    private String diet;

    public UserHibernateModel(Object o, String nombre, String password) {
    }

    public User toDomain() {
        return new User(
                id,
                name,
                email,
                password,
                registerDate,
                plan,
                isValid,
                cookLevel,
                diet
        );
    }
}
