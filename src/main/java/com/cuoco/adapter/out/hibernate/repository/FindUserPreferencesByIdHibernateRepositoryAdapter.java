package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.UserPreferencesHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FindUserPreferencesByIdHibernateRepositoryAdapter extends JpaRepository<UserPreferencesHibernateModel, Long> {}
