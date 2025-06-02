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
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getRegisterDate(),
                user.getPlan(),
                user.getIsValid(),
                user.getCookLevel(),
                user.getDiet()
        );
    }


    private User buildUser(UserHibernateModel userResponse) {
        return new User(
                userResponse.getId(),
                userResponse.getName(),
                userResponse.getEmail(),
                userResponse.getPassword(),
                userResponse.getRegisterDate(),
                userResponse.getPlan(),
                userResponse.getIsValid(),
                userResponse.getCookLevel(),
                userResponse.getDiet()
        );
    }
}