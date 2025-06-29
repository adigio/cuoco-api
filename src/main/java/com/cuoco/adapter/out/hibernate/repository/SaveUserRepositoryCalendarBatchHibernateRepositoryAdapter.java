package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.UserRecipesCalendarHibernateModel;
import com.cuoco.application.usecase.model.UserRecipeCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaveUserRepositoryCalendarBatchHibernateRepositoryAdapter extends JpaRepository<UserRecipesCalendarHibernateModel, Long> {
}
