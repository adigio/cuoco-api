package com.cuoco.adapter.out.hibernate;

import com.cuoco.application.usecase.model.User;
import com.cuoco.application.port.out.CreateUserRepository;
import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import org.springframework.stereotype.Repository;

@Repository
public class CreateUserDatabaseRepositoryAdapter implements CreateUserRepository {

    private final CreateUserHibernateRepositoryAdapter createUserHibernateRepositoryAdapter;

    public CreateUserDatabaseRepositoryAdapter(CreateUserHibernateRepositoryAdapter createUserHibernateRepositoryAdapter) {
        this.createUserHibernateRepositoryAdapter = createUserHibernateRepositoryAdapter;
    }

    @Override
    public User execute(User user) {

        UserHibernateModel userResponse = createUserHibernateRepositoryAdapter.save(buildHibernateUser(user));

        return buildUser(userResponse);
    }

    private UserHibernateModel buildHibernateUser(User user) {
        return new UserHibernateModel(
                null,
                user.getNombre(),
                user.getPassword()
        );
    }

    private User buildUser(UserHibernateModel userResponse) {
        return new User(
                null,
                null,
                null,
                userResponse.getNombre(),
                userResponse.getPassword()
        );
    }
}