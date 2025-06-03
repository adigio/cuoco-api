package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.User;
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
    private Integer  id;
    private String name;
    private String email;
    private String password;
    private LocalDate registerDate;
    private String plan;
    private Boolean isValid;
    private String cookLevel;
    private String diet;

    @ManyToMany
    @JoinTable(
            name = "user_dietary_need",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "dietary_need_id")
    )
    private List<DietaryNeedHibernateModel> dietaryNeeds;


    public User toDomain() {
        List<String> dietaryNeedNames = dietaryNeeds.stream()
                .map(DietaryNeedHibernateModel::getName)
                .collect(Collectors.toList());

        return new User(
                id,
                name,
                email,
                password,
                registerDate,
                plan,
                isValid,
                cookLevel,
                diet,
                dietaryNeedNames
        );
    }
}
