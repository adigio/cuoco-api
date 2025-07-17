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
import com.cuoco.application.port.out.UpdateUserRepository;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserPreferences;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public class UpdateUserDatabaseRepositoryAdapter implements UpdateUserRepository {

    private final CreateUserHibernateRepositoryAdapter createUserHibernateRepositoryAdapter;
    private final CreateUserPreferencesHibernateRepositoryAdapter createUserPreferencesHibernateRepositoryAdapter;

    public UpdateUserDatabaseRepositoryAdapter(
            CreateUserHibernateRepositoryAdapter createUserHibernateRepositoryAdapter,
            CreateUserPreferencesHibernateRepositoryAdapter createUserPreferencesHibernateRepositoryAdapter
    ) {
        this.createUserHibernateRepositoryAdapter = createUserHibernateRepositoryAdapter;
        this.createUserPreferencesHibernateRepositoryAdapter = createUserPreferencesHibernateRepositoryAdapter;
    }

    @Override
    public User execute(User user) {

        UserHibernateModel userToUpdate = buildUserHibernateModel(user);

        UserHibernateModel savedUser = createUserHibernateRepositoryAdapter.save(userToUpdate);
        UserPreferencesHibernateModel savedPreferences = savePreferences(user, savedUser);

        User userResponse = savedUser.toDomain();
        UserPreferences preferences = savedPreferences.toDomain();

        userResponse.setPreferences(preferences);
        userResponse.setDietaryNeeds(user.getDietaryNeeds());
        userResponse.setAllergies(user.getAllergies());

        return userResponse;
    }

    private UserPreferencesHibernateModel savePreferences(User user, UserHibernateModel savedUser) {
        UserPreferencesHibernateModel preferences = buildUserPreferences(user.getPreferences(), savedUser);
        return createUserPreferencesHibernateRepositoryAdapter.save(preferences);
    }

    private UserHibernateModel buildUserHibernateModel(User user) {
        return UserHibernateModel.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .active(user.getActive())
                .plan(PlanHibernateModel.fromDomain(user.getPlan()))
                .dietaryNeeds(user.getDietaryNeeds() != null ? user.getDietaryNeeds().stream().map(DietaryNeedHibernateModel::fromDomain).toList() : List.of())
                .allergies(user.getAllergies() != null ? user.getAllergies().stream().map(AllergyHibernateModel::fromDomain).toList() : List.of())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private UserPreferencesHibernateModel buildUserPreferences(UserPreferences userPreferences, UserHibernateModel savedUser) {
        return UserPreferencesHibernateModel.builder()
                .id(userPreferences.getId())
                .user(savedUser)
                .diet(DietHibernateModel.fromDomain(userPreferences.getDiet()))
                .cookLevel(CookLevelHibernateModel.fromDomain(userPreferences.getCookLevel()))
                .build();
    }
} 