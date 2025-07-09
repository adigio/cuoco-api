package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.UserMealPrepHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeleteUserMealPrepsHibernateRepositoryAdapter extends JpaRepository<UserMealPrepHibernateModel, Long> {
    void deleteAllByUserIdAndMealPrepId(Long userId, Long mealPrepId);
}
