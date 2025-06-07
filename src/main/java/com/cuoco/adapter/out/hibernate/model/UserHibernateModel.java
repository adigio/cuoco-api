package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserPreferences;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user")
public class UserHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private LocalDate registerDate;
    private String plan;
    private Boolean isValid;

    public User toDomain() {
        return new User(
                id,
                name,
                email,
                password,
                registerDate,
                plan,
                isValid,
                null);
    }

}
