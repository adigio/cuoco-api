package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.repository.DeleteUserMealPrepsHibernateRepositoryAdapter;
import com.cuoco.application.port.out.DeleteUserMealPrepRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@Transactional
public class DeleteUserMealPrepDatabaseRepositoryAdapter implements DeleteUserMealPrepRepository {

    private final DeleteUserMealPrepsHibernateRepositoryAdapter deleteUserMealPrepsHibernateRepositoryAdapter;

    public DeleteUserMealPrepDatabaseRepositoryAdapter(DeleteUserMealPrepsHibernateRepositoryAdapter deleteUserMealPrepsHibernateRepositoryAdapter) {
        this.deleteUserMealPrepsHibernateRepositoryAdapter = deleteUserMealPrepsHibernateRepositoryAdapter;
    }

    @Override
    public void execute(Long userId, Long mealPrepId) {
        log.info("Executing delete meal prep from user database repository with user ID {} and meal prep ID {}", userId, mealPrepId);
        deleteUserMealPrepsHibernateRepositoryAdapter.deleteAllByUserIdAndMealPrepId(userId, mealPrepId);
    }
}
