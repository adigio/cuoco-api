package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserExistsByEmailHibernateRepositoryAdapter extends JpaRepository<UserHibernateModel, Long> {
    Boolean existsByEmail(String email);
}
