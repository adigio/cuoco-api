package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.UserPreferences;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "user_preferences")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferencesHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserHibernateModel user;

    @ManyToOne
    @JoinColumn(name = "cook_level_id", referencedColumnName = "id")
    private CookLevelHibernateModel cookLevel;

    @ManyToOne
    @JoinColumn(name = "diet_id", referencedColumnName = "id")
    private DietHibernateModel diet;

    public UserPreferences toDomain() {
        return UserPreferences.builder()
                .cookLevel(cookLevel.toDomain())
                .diet(diet.toDomain())
                .build();
    }
}