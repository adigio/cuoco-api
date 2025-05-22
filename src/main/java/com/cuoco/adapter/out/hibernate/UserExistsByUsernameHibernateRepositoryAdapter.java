package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserExistsByUsernameHibernateRepositoryAdapter extends JpaRepository<UserHibernateModel, Long> {
    Boolean existsByUsername(String username);
}
