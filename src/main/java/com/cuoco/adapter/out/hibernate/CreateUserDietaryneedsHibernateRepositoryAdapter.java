package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.UserDietaryNeedHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreateUserDietaryneedsHibernateRepositoryAdapter extends JpaRepository<UserDietaryNeedHibernateModel, Long> {

}
