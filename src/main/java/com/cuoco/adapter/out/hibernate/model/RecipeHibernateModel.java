package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.Recipe;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "recipe")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String imageUrl;
    private String subtitle;
    private String description;
    @Lob
    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;
    private String preparationTime;

    @ManyToOne
    private CookLevelHibernateModel cookLevel;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RecipeIngredientsHibernateModel> recipeIngredients;

    public Recipe toDomain() {
        return Recipe.builder()
                .id(id)
                .name(name)
                .image(imageUrl)
                .subtitle(subtitle)
                .description(description)
                .instructions(instructions)
                .preparationTime(preparationTime)
                .cookLevel(cookLevel.toDomain())
                .ingredients(
                        recipeIngredients.stream()
                                .map(ri -> ri.getIngredient().toDomain())
                                .toList()
                )
                .build();
    }
}