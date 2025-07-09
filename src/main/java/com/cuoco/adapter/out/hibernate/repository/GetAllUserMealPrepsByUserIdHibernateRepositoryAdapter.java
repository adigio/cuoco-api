package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.UserMealPrepHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GetAllUserMealPrepsByUserIdHibernateRepositoryAdapter extends JpaRepository<UserMealPrepHibernateModel, Long> {
    List<UserMealPrepHibernateModel> findByUserId(Long userId);
}
