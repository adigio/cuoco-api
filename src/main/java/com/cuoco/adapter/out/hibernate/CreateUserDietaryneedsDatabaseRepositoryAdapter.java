package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.DietaryNeedsHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserDietaryNeedHibernateModel;
import com.cuoco.application.port.out.CreateUserDietaryNeedRepository;
import com.cuoco.application.usecase.model.DietaryNeeds;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CreateUserDietaryneedsDatabaseRepositoryAdapter implements CreateUserDietaryNeedRepository {

    private final CreateUserDietaryneedsHibernateRepositoryAdapter createUserDietaryneedsHibernateRepositoryAdapter;

    public CreateUserDietaryneedsDatabaseRepositoryAdapter(CreateUserDietaryneedsHibernateRepositoryAdapter createUserDietaryneedsHibernateRepositoryAdapter) {
        this.createUserDietaryneedsHibernateRepositoryAdapter = createUserDietaryneedsHibernateRepositoryAdapter;
    }

    @Override
    public void execute(Long userId, List<DietaryNeeds> dietaryNeeds) {
        for (DietaryNeeds need : dietaryNeeds) {
            UserDietaryNeedHibernateModel entity = new UserDietaryNeedHibernateModel();
            entity.setUserId(userId);

            DietaryNeedsHibernateModel model = new DietaryNeedsHibernateModel();
            model.setId(need.getId());

            entity.setDietaryNeed(model);
            createUserDietaryneedsHibernateRepositoryAdapter.save(entity);
        }
    }
}
