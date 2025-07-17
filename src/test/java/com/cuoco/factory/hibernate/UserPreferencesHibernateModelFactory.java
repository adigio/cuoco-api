package com.cuoco.factory.hibernate;

import com.cuoco.adapter.out.hibernate.model.CookLevelHibernateModel;
import com.cuoco.adapter.out.hibernate.model.DietHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserPreferencesHibernateModel;

public class UserPreferencesHibernateModelFactory {

    public static UserPreferencesHibernateModel create() {
        return UserPreferencesHibernateModel.builder()
                .id(1L)
                .user(UserHibernateModelFactory.create())
                .cookLevel(CookLevelHibernateModel.builder().id(1).description("Cook Level").build())
                .diet(DietHibernateModel.builder().id(1).description("Diet").build())
                .build();
    }

}
