package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.Step;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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

    private String imageType;
    private String imageName;
    private Integer stepNumber;
    private String stepDescription;

    public Step toDomain() {
        return Step.builder()
                .id(id)
                .imageName(imageName)
                .build();
    }
}
