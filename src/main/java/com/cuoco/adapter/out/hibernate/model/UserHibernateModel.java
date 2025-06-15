package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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