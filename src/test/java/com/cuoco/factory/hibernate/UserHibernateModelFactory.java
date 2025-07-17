package com.cuoco.factory.hibernate;

import com.cuoco.adapter.out.hibernate.model.PlanHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;

import java.time.LocalDateTime;

public class UserHibernateModelFactory {

    public static UserHibernateModel create() {
        return UserHibernateModel.builder()
                .id(1L)
                .name("Name")
                .email("email@email.com")
                .password("password")
                .plan(PlanHibernateModel.builder().id(1).description("Plan 1").build())
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

}
