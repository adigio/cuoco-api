package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.UpdateUserActiveHibernateRepositoryAdapter;
import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.out.UpdateUserActiveRepository;
import com.cuoco.application.usecase.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class UpdateUserActiveDatabaseActiveRepositoryAdapter implements UpdateUserActiveRepository {

    private final UpdateUserActiveHibernateRepositoryAdapter updateUserActiveHibernateRepositoryAdapter;

    public UpdateUserActiveDatabaseActiveRepositoryAdapter(UpdateUserActiveHibernateRepositoryAdapter updateUserActiveHibernateRepositoryAdapter) {
        this.updateUserActiveHibernateRepositoryAdapter = updateUserActiveHibernateRepositoryAdapter;
    }

    @Override
    public void execute(User user) {
        UserHibernateModel existingUser = updateUserActiveHibernateRepositoryAdapter.findById(user.getId())
                .orElseThrow(() -> new BadRequestException("Usuario no encontrado"));

        existingUser.setActive(user.getActive());

        updateUserActiveHibernateRepositoryAdapter.save(existingUser);
    }


}
