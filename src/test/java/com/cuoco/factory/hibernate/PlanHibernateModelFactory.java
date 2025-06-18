package com.cuoco.factory.hibernate;

import com.cuoco.adapter.out.hibernate.model.PlanHibernateModel;

public class PlanHibernateModelFactory {

    public static PlanHibernateModel create() {

        return PlanHibernateModel.builder()
                .id(1)
                .description("Free")
                .build();

    }

    public static PlanHibernateModel create(Integer id, String description) {

        return PlanHibernateModel.builder()
                .id(id)
                .description(description)
                .build();

    }
}
