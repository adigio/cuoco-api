package com.cuoco.infrastructure.repository.hibernate;

import com.cuoco.infrastructure.repository.hibernate.model.UserHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface UserExistsByUsernameHibernateRepository extends JpaRepository<UserHibernateModel, Long> {
    Boolean existsByUsername(String username);
}
