package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.UserCalendarsHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GetAllUserCalendarsByUserIdHibernateRepositoryAdapter extends JpaRepository<UserCalendarsHibernateModel, Long> {
    List<UserCalendarsHibernateModel> findAllByUserId(Long id);
}
