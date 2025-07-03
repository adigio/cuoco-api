package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.UserCalendarsHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ExistsUserRecipeCalendarHibernateRepositoryAdapter extends JpaRepository<UserCalendarsHibernateModel, Long> {
    boolean existsByUserIdAndPlannedDateAndRecipesRecipeId(Long userId, LocalDate plannedDate, Long recipeId);
}
