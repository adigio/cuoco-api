package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.MealCategory;
import com.cuoco.application.usecase.model.Recipe;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
    private String subtitle;
    private String description;
    private String imageUrl;
    @Lob
    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;

    @ManyToOne
    private PreparationTimeHibernateModel preparationTime;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private MealTypeHibernateModel type;

    @ManyToOne
    private CookLevelHibernateModel cookLevel;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RecipeIngredientsHibernateModel> ingredients;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RecipeMealCategoriesHibernateModel> categories;

    public Recipe toDomain() {
        List<Ingredient> domainIngredients = ingredients.stream()
                .map(ri -> ri.toDomain().getIngredient())
                .toList();

        List<MealCategory> domainCategories = categories.stream()
                .map(c -> c.toDomain().getCategory())
                .toList();

        return Recipe.builder()
                .id(id)
                .name(name)
                .image(imageUrl)
                .subtitle(subtitle)
                .description(description)
                .instructions(instructions)
                .preparationTime(preparationTime.toDomain())
                .type(type.toDomain())
                .cookLevel(cookLevel.toDomain())
                .categories(domainCategories)
                .ingredients(domainIngredients)
                .build();
    }
}