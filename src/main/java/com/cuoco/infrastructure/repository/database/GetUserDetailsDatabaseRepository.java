package com.cuoco.infrastructure.repository.database;

import com.cuoco.infrastructure.repository.hibernate.GetUserHibernateRepository;
import com.cuoco.infrastructure.repository.hibernate.model.UserHibernateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GetUserDetailsDatabaseRepository implements UserDetailsService {

    @Autowired
    private GetUserHibernateRepository getUserHibernateRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserHibernateModel user = getUserHibernateRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return new User(
                user.getUsername(),
                user.getPassword(),
                Collections.emptyList()
        );
    }
}
