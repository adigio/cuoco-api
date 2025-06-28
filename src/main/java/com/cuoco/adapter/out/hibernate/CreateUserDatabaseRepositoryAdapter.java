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
import com.cuoco.adapter.out.hibernate.repository.UserAllergiesRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.UserDietaryNeedsRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.CreateUserHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.CreateUserPreferencesHibernateRepositoryAdapter;
import com.cuoco.application.port.out.CreateUserRepository;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserPreferences;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public class CreateUserDatabaseRepositoryAdapter implements CreateUserRepository {

    private final CreateUserHibernateRepositoryAdapter createUserHibernateRepositoryAdapter;
    private final CreateUserPreferencesHibernateRepositoryAdapter createUserPreferencesHibernateRepositoryAdapter;
    private final UserDietaryNeedsRepositoryAdapter userDietaryNeedsRepositoryAdapter;
    private final UserAllergiesRepositoryAdapter userAllergiesRepositoryAdapter;

    public CreateUserDatabaseRepositoryAdapter(
            CreateUserHibernateRepositoryAdapter createUserHibernateRepositoryAdapter,
            CreateUserPreferencesHibernateRepositoryAdapter createUserPreferencesHibernateRepositoryAdapter,
            UserDietaryNeedsRepositoryAdapter userDietaryNeedsRepositoryAdapter,
            UserAllergiesRepositoryAdapter userAllergiesRepositoryAdapter
    ) {
        this.createUserHibernateRepositoryAdapter = createUserHibernateRepositoryAdapter;
        this.createUserPreferencesHibernateRepositoryAdapter = createUserPreferencesHibernateRepositoryAdapter;
        this.userDietaryNeedsRepositoryAdapter = userDietaryNeedsRepositoryAdapter;
        this.userAllergiesRepositoryAdapter = userAllergiesRepositoryAdapter;
    }

    @Override
    public User execute(User user) {

        UserHibernateModel userToSave = buildHibernateUser(user);

        UserHibernateModel savedUser = createUserHibernateRepositoryAdapter.save(userToSave);

        UserPreferencesHibernateModel savedPreferences = savePreferences(user, savedUser);
        
        saveDietaryNeeds(user, savedUser);

        saveAllergies(user, savedUser);

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

    private void saveAllergies(User user, UserHibernateModel savedUser) {
        List<UserAllergiesHibernateModel> allergies = user.getAllergies()
                .stream()
                .map(allergy -> buildUserAllergies(savedUser, allergy))
                .toList();

                    userAllergiesRepositoryAdapter.saveAll(allergies);
    }

    private void saveDietaryNeeds(User user, UserHibernateModel savedUser) {
        List<UserDietaryNeedsHibernateModel> dietaryNeeds = user.getDietaryNeeds()
                .stream()
                .map(dietaryNeed -> buildUserDietaryNeedsHibernateModel(savedUser, dietaryNeed))
                .toList();

                    userDietaryNeedsRepositoryAdapter.saveAll(dietaryNeeds);
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
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );
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