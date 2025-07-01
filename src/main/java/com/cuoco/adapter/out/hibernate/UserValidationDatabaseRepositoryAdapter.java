package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.repository.UserValidationRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserValidationDatabaseRepositoryAdapter implements UserValidationRepository {

    private final EntityManager entityManager;

    @Override
    @Transactional
    public void setUserValid(Long userId) {
        entityManager.createQuery("UPDATE UserHibernateModel u SET u.active = true WHERE u.id = :id")
                .setParameter("id", userId)
                .executeUpdate();
    }
}
