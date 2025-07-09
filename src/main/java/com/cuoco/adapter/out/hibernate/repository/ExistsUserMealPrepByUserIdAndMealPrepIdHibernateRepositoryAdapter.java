package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.UserMealPrepHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExistsUserMealPrepByUserIdAndMealPrepIdHibernateRepositoryAdapter extends JpaRepository<UserMealPrepHibernateModel, Long> {
    boolean existsByUserIdAndMealPrepId(Long userId, Long mealPrepId);
}
