package com.cuoco.factory.hibernate;

import com.cuoco.adapter.out.hibernate.model.DietaryNeedHibernateModel;

public class DietaryNeedHibernateModelFactory {

    public static DietaryNeedHibernateModel create() {

        return DietaryNeedHibernateModel.builder()
                .id(1)
                .description("Dieta")
                .build();

    }

    public static DietaryNeedHibernateModel create(Integer id, String description) {

        return DietaryNeedHibernateModel.builder()
                .id(id)
                .description(description)
                .build();

    }

}
