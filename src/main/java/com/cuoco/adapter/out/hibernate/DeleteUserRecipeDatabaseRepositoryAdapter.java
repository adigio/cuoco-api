package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.repository.DeleteUserRecipeHibernateRepositoryAdapter;
import com.cuoco.application.port.out.DeleteUserRecipeRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@Transactional
public class DeleteUserRecipeDatabaseRepositoryAdapter implements DeleteUserRecipeRepository {

    private final DeleteUserRecipeHibernateRepositoryAdapter deleteUserRecipeHibernateRepositoryAdapter;

    public DeleteUserRecipeDatabaseRepositoryAdapter(DeleteUserRecipeHibernateRepositoryAdapter deleteUserRecipeHibernateRepositoryAdapter) {
        this.deleteUserRecipeHibernateRepositoryAdapter = deleteUserRecipeHibernateRepositoryAdapter;
    }

    @Override
    public void execute(Long userId, Long recipeId) {
        log.info("Executing delete recipe from user database repository with user ID {} and recipe ID {}", userId, recipeId);

        deleteUserRecipeHibernateRepositoryAdapter.deleteAllByUserIdAndRecipeId(userId, recipeId);
    }
}
