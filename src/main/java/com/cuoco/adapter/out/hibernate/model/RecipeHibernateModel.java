package com.cuoco.adapter.out.hibernate.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "recipe")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String steps;
    private String imageUrl;
    private Integer estimatedTime;

    @ManyToOne
    private CookLevelHibernateModel cookLevel;

}