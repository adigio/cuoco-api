package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.DietaryNeedHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GetDietaryNeedsByIdHibernateRepositoryAdapter extends JpaRepository<DietaryNeedHibernateModel, Long> {
    List<DietaryNeedHibernateModel> findByIdIn(List<Integer> dietaryNeedIds);
}
