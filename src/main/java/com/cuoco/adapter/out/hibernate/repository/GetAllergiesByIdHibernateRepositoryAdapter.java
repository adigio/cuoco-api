package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.AllergyHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GetAllergiesByIdHibernateRepositoryAdapter extends JpaRepository<AllergyHibernateModel, Long> {
    List<AllergyHibernateModel> findByIdIn(List<Integer> allergiesIds);
}
