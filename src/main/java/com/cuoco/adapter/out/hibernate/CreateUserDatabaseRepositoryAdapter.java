package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.AllergyHibernateModel;
import com.cuoco.adapter.out.hibernate.model.CookLevelHibernateModel;
import com.cuoco.adapter.out.hibernate.model.DietHibernateModel;
import com.cuoco.adapter.out.hibernate.model.DietaryNeedHibernateModel;
import com.cuoco.adapter.out.hibernate.model.PlanHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserPreferencesHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.CreateUserHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.CreateUserPreferencesHibernateRepositoryAdapter;
import com.cuoco.application.port.out.CreateUserRepository;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserPreferences;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@Transactional
public class CreateUserDatabaseRepositoryAdapter implements CreateUserRepository {

    private final CreateUserHibernateRepositoryAdapter createUserHibernateRepositoryAdapter;
    private final CreateUserPreferencesHibernateRepositoryAdapter createUserPreferencesHibernateRepositoryAdapter;

    public CreateUserDatabaseRepositoryAdapter(
            CreateUserHibernateRepositoryAdapter createUserHibernateRepositoryAdapter,
            CreateUserPreferencesHibernateRepositoryAdapter createUserPreferencesHibernateRepositoryAdapter
    ) {
        this.createUserHibernateRepositoryAdapter = createUserHibernateRepositoryAdapter;
        this.createUserPreferencesHibernateRepositoryAdapter = createUserPreferencesHibernateRepositoryAdapter;
    }

    @Override
    public User execute(User user) {

        UserHibernateModel userToSave = buildHibernateUser(user);

        UserHibernateModel savedUser = createUserHibernateRepositoryAdapter.save(userToSave);

        UserPreferencesHibernateModel savedPreferences = savePreferences(user, savedUser);

        User userResponse = savedUser.toDomain();
        UserPreferences preferences = savedPreferences.toDomain();

        userResponse.setPreferences(preferences);
        userResponse.setDietaryNeeds(user.getDietaryNeeds());
        userResponse.setAllergies(user.getAllergies());

        return userResponse;
    }

    private UserPreferencesHibernateModel savePreferences(User user, UserHibernateModel savedUser) {
        UserPreferencesHibernateModel preferences = buildUserPreferences(user, savedUser);
        return createUserPreferencesHibernateRepositoryAdapter.save(preferences);
    }

    private UserHibernateModel buildHibernateUser(User user) {
        return UserHibernateModel.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .plan(PlanHibernateModel.fromDomain(user.getPlan()))
                .active(user.getActive())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .dietaryNeeds(user.getDietaryNeeds().stream().map(DietaryNeedHibernateModel::fromDomain).toList())
                .allergies(user.getAllergies().stream().map(AllergyHibernateModel::fromDomain).toList())
                .build();
    }

    private UserPreferencesHibernateModel buildUserPreferences(User user, UserHibernateModel savedUser) {
        return new UserPreferencesHibernateModel(
                null,
                savedUser,
                new CookLevelHibernateModel(
                        user.getPreferences().getCookLevel().getId(),
                        user.getPreferences().getCookLevel().getDescription()
                ),
                new DietHibernateModel(
                        user.getPreferences().getDiet().getId(),
                        user.getPreferences().getDiet().getDescription()
                )
        );
    }
}