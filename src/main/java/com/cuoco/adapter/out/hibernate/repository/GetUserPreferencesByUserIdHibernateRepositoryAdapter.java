package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.UserPreferencesHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GetUserPreferencesByUserIdHibernateRepositoryAdapter extends JpaRepository<UserPreferencesHibernateModel, Long> {
    Optional<UserPreferencesHibernateModel> getByUserId(Long userId);
}
