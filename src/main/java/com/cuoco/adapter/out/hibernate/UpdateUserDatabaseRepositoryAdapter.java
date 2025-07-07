package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.UpdateUserHibernateRepositoryAdapter;
import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.out.UpdateUserRepository;
import com.cuoco.application.usecase.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class UpdateUserDatabaseRepositoryAdapter implements UpdateUserRepository {

    private final UpdateUserHibernateRepositoryAdapter updateUserHibernateRepositoryAdapter;

    public UpdateUserDatabaseRepositoryAdapter(UpdateUserHibernateRepositoryAdapter updateUserHibernateRepositoryAdapter) {
        this.updateUserHibernateRepositoryAdapter = updateUserHibernateRepositoryAdapter;
    }

    @Override
    public void execute(User user) {
        UserHibernateModel existingUser = updateUserHibernateRepositoryAdapter.findById(user.getId())
                .orElseThrow(() -> new BadRequestException("Usuario no encontrado"));

        existingUser.setActive(user.getActive());

        updateUserHibernateRepositoryAdapter.save(existingUser);
    }


}
