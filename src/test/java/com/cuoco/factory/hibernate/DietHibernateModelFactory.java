package com.cuoco.factory.hibernate;

import com.cuoco.adapter.out.hibernate.model.DietHibernateModel;

public class DietHibernateModelFactory {

    public static DietHibernateModel create() {

        return DietHibernateModel.builder()
                .id(1)
                .description("Dieta")
                .build();

    }

    public static DietHibernateModel create(Integer id, String description) {

        return DietHibernateModel.builder()
                .id(id)
                .description(description)
                .build();

    }

}
