package com.cuoco.adapter.out.hibernate.model;


import com.cuoco.application.usecase.model.Step;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "meal_prep_steps")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealPrepStepsHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "meal_prep_id", referencedColumnName = "id")
    private MealPrepHibernateModel mealPrep;

    private String title;
    private Integer number;
    @Lob
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    private String time;
    private String imageName;

    public Step toDomain() {
        return Step.builder()
                .id(id)
                .time(title)
                .number(number)
                .description(description)
                .time(time)
                .imageName(imageName)
                .build();
    }
}
