package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.UserRecipesCalendarHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface UserRecipeCalendarExistHibernateRepositoryAdapter extends JpaRepository<UserRecipesCalendarHibernateModel, Long> {


    Boolean existsByUserIdAndRecipeIdAndPlannedDate(Long userId, Long recipeId, LocalDate plannedDate);
}
