package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.Calendar;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity(name = "user_calendars")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCalendarsHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate plannedDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserHibernateModel user;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCalendarRecipesHibernateModel> recipes;

    public Calendar toDomain() {
        return Calendar.builder()
                .date(plannedDate)
                .recipes(recipes.stream().map(UserCalendarRecipesHibernateModel::toDomain).toList())
                .build();
    }
}
