package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.RecipeImage;
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

@Entity(name = "recipe_steps_images")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeStepsImagesHibernateModel {

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

    public RecipeImage toDomain() {
        return RecipeImage.builder()
                .id(id)
                .imageName(imageName)
                .imageType(imageType)
                .stepNumber(stepNumber)
                .stepDescription(stepDescription)
                .build();
    }
}
