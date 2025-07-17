package com.cuoco.factory.hibernate;

import com.cuoco.adapter.out.hibernate.model.CookLevelHibernateModel;

public class CookLevelHibernateModelFactory {

    public static CookLevelHibernateModel create() {

        return CookLevelHibernateModel.builder()
                .id(1)
                .description("Medium")
                .build();

    }

    public static CookLevelHibernateModel create(Integer id, String description) {

        return CookLevelHibernateModel.builder()
                .id(id)
                .description(description)
                .build();

    }
}
