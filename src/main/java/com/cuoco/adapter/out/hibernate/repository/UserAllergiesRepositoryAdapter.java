package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.UserAllergiesHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAllergiesRepositoryAdapter extends JpaRepository<UserAllergiesHibernateModel, Long> {
    
    void deleteByUser_Id(Long userId);
} 