package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.UserDietaryNeedsHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDietaryNeedsRepositoryAdapter extends JpaRepository<UserDietaryNeedsHibernateModel, Long> {
    
    void deleteByUser_Id(Long userId);
} 