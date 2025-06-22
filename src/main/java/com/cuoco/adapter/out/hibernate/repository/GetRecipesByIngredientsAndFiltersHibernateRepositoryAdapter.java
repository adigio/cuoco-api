package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GetRecipesByIngredientsAndFiltersHibernateRepositoryAdapter extends JpaRepository<RecipeHibernateModel, Long> {

    @Query("""
        SELECT DISTINCT r FROM recipe r
        LEFT JOIN r.categories rc
        LEFT JOIN rc.category c
        WHERE r.id IN :recipeIds
            AND (:preparationTimeId IS NULL OR r.preparationTime.id = :preparationTimeId)
            AND (:cookLevelId IS NULL OR r.cookLevel.id = :cookLevelId)
            AND (:typesIds IS NULL OR r.type.id IN :typesIds)
            AND (:categoriesIds IS NULL OR c.id IN :categoriesIds)
    """)
    List<RecipeHibernateModel> execute(
            @Param("recipeIds") List<Long> recipeIds,
            @Param("preparationTimeId") Integer preparationTimeId,
            @Param("cookLevelId") Integer cookLevelId,
            @Param("typesIds") List<Integer> typesIds,
            @Param("categoriesIds") List<Integer> categoriesIds,
            Pageable pageable
    );
}
