package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.AllergyHibernateModel;
import com.cuoco.adapter.out.hibernate.model.CookLevelHibernateModel;
import com.cuoco.adapter.out.hibernate.model.DietHibernateModel;
import com.cuoco.adapter.out.hibernate.model.DietaryNeedHibernateModel;
import com.cuoco.adapter.out.hibernate.model.PlanHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserAllergiesHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserDietaryNeedsHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserPreferencesHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.CreateUserAllergiesHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.CreateUserDietaryNeedsHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.CreateUserHibernateRepositoryAdapter;
import com.cuoco.application.port.out.CreateUserRepository;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Repository
public class CreateUserDatabaseRepositoryAdapter implements CreateUserRepository {

    private final CreateUserHibernateRepositoryAdapter createUserHibernateRepositoryAdapter;
    private final CreateUserDietaryNeedsHibernateRepositoryAdapter createUserDietaryNeedsHibernateRepositoryAdapter;
    private final CreateUserAllergiesHibernateRepositoryAdapter createUserAllergiesHibernateRepositoryAdapter;

    public CreateUserDatabaseRepositoryAdapter(
            CreateUserHibernateRepositoryAdapter createUserHibernateRepositoryAdapter,
            CreateUserDietaryNeedsHibernateRepositoryAdapter createUserDietaryNeedsHibernateRepositoryAdapter,
            CreateUserAllergiesHibernateRepositoryAdapter createUserAllergiesHibernateRepositoryAdapter
    ) {
        this.createUserHibernateRepositoryAdapter = createUserHibernateRepositoryAdapter;
        this.createUserDietaryNeedsHibernateRepositoryAdapter = createUserDietaryNeedsHibernateRepositoryAdapter;
        this.createUserAllergiesHibernateRepositoryAdapter = createUserAllergiesHibernateRepositoryAdapter;
    }

    @Override
    public User execute(User user) {

        UserHibernateModel userToSave = buildHibernateUser(user);

        UserHibernateModel savedUser = createUserHibernateRepositoryAdapter.save(userToSave);

        saveDietaryNeeds(user, savedUser);

        saveAllergies(user, savedUser);

        User userResponse = savedUser.toDomain();

        userResponse.setDietaryNeeds(user.getDietaryNeeds());
        userResponse.setAllergies(user.getAllergies());

        return userResponse;
    }

    private void saveAllergies(User user, UserHibernateModel savedUser) {
        List<UserAllergiesHibernateModel> allergies = user.getAllergies()
                .stream()
                .map(allergy -> buildUserAllergies(savedUser, allergy))
                .toList();

        List<UserAllergiesHibernateModel> allergiesResponse = createUserAllergiesHibernateRepositoryAdapter.saveAll(allergies);
    }

    private void saveDietaryNeeds(User user, UserHibernateModel savedUser) {
        List<UserDietaryNeedsHibernateModel> dietaryNeeds = user.getDietaryNeeds()
                .stream()
                .map(dietaryNeed -> buildUserDietaryNeedsHibernateModel(savedUser, dietaryNeed))
                .toList();

        List<UserDietaryNeedsHibernateModel> dietaryNeedsResponse = createUserDietaryNeedsHibernateRepositoryAdapter.saveAll(dietaryNeeds);
    }

    private UserHibernateModel buildHibernateUser(User user) {
        return new UserHibernateModel(
                null,
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                new PlanHibernateModel(
                        user.getPlan().getId(),
                        user.getPlan().getDescription()
                ),
                user.getActive(),
                buildUserPreferences(user),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );
    }

    private UserPreferencesHibernateModel buildUserPreferences(User user) {
        return new UserPreferencesHibernateModel(
                null,
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

    private UserDietaryNeedsHibernateModel buildUserDietaryNeedsHibernateModel(UserHibernateModel user, DietaryNeed dietaryNeed) {
        return UserDietaryNeedsHibernateModel.builder()
                .user(user)
                .dietaryNeed(buildDietaryNeedHibernateModel(dietaryNeed))
                .build();
    }

    private DietaryNeedHibernateModel buildDietaryNeedHibernateModel(DietaryNeed dietaryNeed) {
        return DietaryNeedHibernateModel.builder()
                .description(dietaryNeed.getDescription())
                .build();
    }

    private UserAllergiesHibernateModel buildUserAllergies(UserHibernateModel user, Allergy allergy) {
        return UserAllergiesHibernateModel.builder()
                .user(user)
                .allergy(buildAllergy(allergy))
                .build();
    }

    private AllergyHibernateModel buildAllergy(Allergy allergy) {
        return AllergyHibernateModel.builder()
                .description(allergy.getDescription())
                .build();
    }
}