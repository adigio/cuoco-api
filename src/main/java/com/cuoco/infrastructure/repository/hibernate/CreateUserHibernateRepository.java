package com.cuoco.infrastructure.repository.hibernate;

import com.cuoco.domain.model.User;
import com.cuoco.domain.port.repository.CreateUserRepository;
import com.cuoco.infrastructure.repository.hibernate.model.UserHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreateUserHibernateRepository extends JpaRepository<UserHibernateModel, Long> {
    UserHibernateModel save(User user);
}
