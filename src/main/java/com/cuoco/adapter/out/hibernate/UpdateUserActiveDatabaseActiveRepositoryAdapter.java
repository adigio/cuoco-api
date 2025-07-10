package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.UpdateUserActiveHibernateRepositoryAdapter;
import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.out.UpdateUserActiveRepository;
import com.cuoco.application.usecase.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UpdateUserActiveDatabaseActiveRepositoryAdapter implements UpdateUserActiveRepository {

    private final UpdateUserActiveHibernateRepositoryAdapter updateUserActiveHibernateRepositoryAdapter;

    @Override
    public void execute(User user) {
        UserHibernateModel existingUser = updateUserActiveHibernateRepositoryAdapter.findById(user.getId())
                .orElseThrow(() -> new BadRequestException("Usuario no encontrado"));

        existingUser.setActive(user.getActive());

        updateUserActiveHibernateRepositoryAdapter.save(existingUser);
    }
}
