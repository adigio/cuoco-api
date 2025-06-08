package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.UserDietaryNeedsHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreateUserDietaryNeedsHibernateRepositoryAdapter extends JpaRepository<UserDietaryNeedsHibernateModel, Long> {}
