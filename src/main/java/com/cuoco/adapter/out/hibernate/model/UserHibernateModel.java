package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    @OneToOne
    @JoinColumn(name = "plan_id")
    private PlanHibernateModel plan;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @ManyToMany
    @JoinTable(
            name = "user_allergies",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "allergy_id")
    )
    private List<AllergyHibernateModel> allergies;

    @ManyToMany
    @JoinTable(
            name = "user_dietary_needs",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "dietary_need_id")
    )
    private List<DietaryNeedHibernateModel> dietaryNeeds;

    @OneToMany(mappedBy = "user")
    private List<UserRecipesHibernateModel> recipes;

    @ManyToMany
    @JoinTable(
            name = "user_meal_preps",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "meal_prep_id")
    )
    private List<MealPrepHibernateModel> mealPreps;

    @OneToMany(mappedBy = "user")
    private List<UserCalendarsHibernateModel> calendars;

    public User toDomain() {
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .password(password)
                .plan(plan.toDomain())
                .active(active)
                .createdAt(createdAt)
                .build();
    }
}