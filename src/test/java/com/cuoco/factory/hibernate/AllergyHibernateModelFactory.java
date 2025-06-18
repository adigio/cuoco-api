package com.cuoco.factory.hibernate;

import com.cuoco.adapter.out.hibernate.model.AllergyHibernateModel;

public class AllergyHibernateModelFactory {

    public static AllergyHibernateModel create() {

        return AllergyHibernateModel.builder()
                .id(1)
                .description("Allergy")
                .build();

    }

    public static AllergyHibernateModel create(Integer id, String description) {

        return AllergyHibernateModel.builder()
                .id(id)
                .description(description)
                .build();

    }

}
