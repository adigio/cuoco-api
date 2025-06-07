package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.AllergiesHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserAllergiesHibernateModel;
import com.cuoco.application.port.out.CreateUserAllergieRepository;
import com.cuoco.application.usecase.model.Allergies;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CreateUserAllergiesDatabaseRepositoryAdapter implements CreateUserAllergieRepository {
    private final CreateUserAllergiesHibernateRepositoryAdapter createUserAllergiesHibernateRepositoryAdapter;

    public CreateUserAllergiesDatabaseRepositoryAdapter(CreateUserAllergiesHibernateRepositoryAdapter createUserAllergiesHibernateRepositoryAdapter) {
        this.createUserAllergiesHibernateRepositoryAdapter = createUserAllergiesHibernateRepositoryAdapter;
    }

    @Override
    public void execute(Long userId, List<Allergies> allergies) {
        for (Allergies need : allergies) {
            UserAllergiesHibernateModel entity = new UserAllergiesHibernateModel();
            entity.setUserId(userId);

            AllergiesHibernateModel model = new AllergiesHibernateModel();
            model.setId(need.getId());

            entity.setAllergies(model);
            createUserAllergiesHibernateRepositoryAdapter.save(entity);
        }
    }
}
