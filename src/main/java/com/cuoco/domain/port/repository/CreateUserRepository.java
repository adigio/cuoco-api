package com.cuoco.domain.port.repository;

import com.cuoco.domain.model.User;
import com.cuoco.infrastructure.repository.hibernate.model.UserHibernateModel;

public interface CreateUserRepository {
    User execute(User user);
}
