package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.AllergiesHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FindAllergiesByNameHibernateRepositoryAdapter extends JpaRepository<AllergiesHibernateModel, Long> {
    List<AllergiesHibernateModel> findByNameIn(List<String> allergiesNames);
}
