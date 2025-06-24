package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.Recipe;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "recipes")
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
    private CookLevelHibernateModel cookLevel;

    @ManyToOne
    private DietHibernateModel diet;

    @ManyToMany
    @JoinTable(
            name = "recipe_meal_types",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "meal_type_id")
    )
    private List<MealTypeHibernateModel> mealTypes;

    @ManyToMany
    @JoinTable(
            name = "recipe_allergies",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "allergy_id")
    )
    private List<AllergyHibernateModel> allergies;

    @ManyToMany
    @JoinTable(
            name = "recipe_dietary_needs",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "dietary_need_id")
    )
    private List<DietaryNeedHibernateModel> dietaryNeeds;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RecipeIngredientsHibernateModel> ingredients;

    public Recipe toDomain() {
        List<Ingredient> domainIngredients = ingredients.stream()
                .map(ri -> ri.toDomain().getIngredient())
                .toList();

        return Recipe.builder()
                .id(id)
                .name(name)
                .subtitle(subtitle)
                .description(description)
                .instructions(instructions)
                .image(imageUrl)
                .preparationTime(preparationTime.toDomain())
                .cookLevel(cookLevel.toDomain())
                .diet(diet != null ? diet.toDomain() : null)
                .mealTypes(mealTypes.stream().map(MealTypeHibernateModel::toDomain).toList())
                .allergies(allergies.stream().map(AllergyHibernateModel::toDomain).toList())
                .dietaryNeeds(dietaryNeeds.stream().map(DietaryNeedHibernateModel::toDomain).toList())
                .ingredients(domainIngredients)
                .build();
    }
}