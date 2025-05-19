package com.cuoco.infrastructure.repository.database;

import com.cuoco.domain.model.User;
import com.cuoco.domain.port.repository.CreateUserRepository;
import com.cuoco.infrastructure.repository.hibernate.CreateUserHibernateRepository;
import com.cuoco.infrastructure.repository.hibernate.model.UserHibernateModel;
import org.springframework.stereotype.Repository;

@Repository
public class CreateUserDatabaseRepository implements CreateUserRepository {

    private final CreateUserHibernateRepository createUserHibernateRepository;

    public CreateUserDatabaseRepository(CreateUserHibernateRepository createUserHibernateRepository) {
        this.createUserHibernateRepository = createUserHibernateRepository;
    }

    @Override
    public User execute(User user) {

        UserHibernateModel userResponse = createUserHibernateRepository.save(user);

        return buildUser(userResponse);
    }

    private User buildUser(UserHibernateModel userResponse) {
        return new User(
                null,
                null,
                null,
                userResponse.getUsername(),
                userResponse.getPassword(),
                null
        );
    }
}