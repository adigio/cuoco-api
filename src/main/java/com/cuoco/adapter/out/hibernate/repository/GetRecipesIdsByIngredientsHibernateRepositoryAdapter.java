package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GetRecipesIdsByIngredientsHibernateRepositoryAdapter extends JpaRepository<RecipeHibernateModel, Long> {

    @Query("""
        SELECT r.id FROM recipe r
        JOIN r.ingredients ri
        JOIN ri.ingredient i
        WHERE LOWER(i.name) IN :ingredientNames
    """)
    List<Long> execute(
            @Param("ingredientNames") List<String> ingredientNames,
            @Param("ingredientCount") Integer ingredientCount
    );
}
