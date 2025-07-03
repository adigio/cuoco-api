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

@Entity(name = "recipe_steps")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeStepsHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private RecipeHibernateModel recipe;

    private Integer number;
    private String title;
    @Lob
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    private String imageName;

    public Step toDomain() {
        return Step.builder()
                .id(id)
                .number(number)
                .title(title)
                .description(description)
                .imageName(imageName)
                .build();
    }
}
