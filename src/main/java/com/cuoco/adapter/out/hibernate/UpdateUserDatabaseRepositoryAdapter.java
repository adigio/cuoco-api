package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.*;
import com.cuoco.adapter.out.hibernate.repository.*;
import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.out.UpdateUserRepository;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.User;
import com.cuoco.shared.model.ErrorDescription;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@Transactional
public class UpdateUserDatabaseRepositoryAdapter implements UpdateUserRepository {

    private final CreateUserHibernateRepositoryAdapter createUserHibernateRepositoryAdapter;
    private final CreateUserPreferencesHibernateRepositoryAdapter createUserPreferencesHibernateRepositoryAdapter;
    private final UserDietaryNeedsRepositoryAdapter userDietaryNeedsRepositoryAdapter;
    private final UserAllergiesRepositoryAdapter userAllergiesRepositoryAdapter;
    private final FindUserByEmailHibernateRepositoryAdapter findUserByEmailHibernateRepositoryAdapter;
    private final FindUserPreferencesByIdHibernateRepositoryAdapter findUserPreferencesByIdHibernateRepositoryAdapter;
    private final GetDietaryNeedsByIdHibernateRepositoryAdapter getDietaryNeedsByIdHibernateRepositoryAdapter;
    private final GetAllergiesByIdHibernateRepositoryAdapter getAllergiesByIdHibernateRepositoryAdapter;
    private final GetDietByIdHibernateRepositoryAdapter getDietByIdHibernateRepositoryAdapter;
    private final GetCookLevelByIdHibernateRepositoryAdapter getCookLevelByIdHibernateRepositoryAdapter;
    private final GetPlanByIdHibernateRepositoryAdapter getPlanByIdHibernateRepositoryAdapter;

    public UpdateUserDatabaseRepositoryAdapter(
            CreateUserHibernateRepositoryAdapter createUserHibernateRepositoryAdapter,
            CreateUserPreferencesHibernateRepositoryAdapter createUserPreferencesHibernateRepositoryAdapter,
            UserDietaryNeedsRepositoryAdapter userDietaryNeedsRepositoryAdapter,
            UserAllergiesRepositoryAdapter userAllergiesRepositoryAdapter,
            FindUserByEmailHibernateRepositoryAdapter findUserByEmailHibernateRepositoryAdapter,
            FindUserPreferencesByIdHibernateRepositoryAdapter findUserPreferencesByIdHibernateRepositoryAdapter,
            GetDietaryNeedsByIdHibernateRepositoryAdapter getDietaryNeedsByIdHibernateRepositoryAdapter,
            GetAllergiesByIdHibernateRepositoryAdapter getAllergiesByIdHibernateRepositoryAdapter,
            GetDietByIdHibernateRepositoryAdapter getDietByIdHibernateRepositoryAdapter,
            GetCookLevelByIdHibernateRepositoryAdapter getCookLevelByIdHibernateRepositoryAdapter,
            GetPlanByIdHibernateRepositoryAdapter getPlanByIdHibernateRepositoryAdapter
    ) {
        this.createUserHibernateRepositoryAdapter = createUserHibernateRepositoryAdapter;
        this.createUserPreferencesHibernateRepositoryAdapter = createUserPreferencesHibernateRepositoryAdapter;
        this.userDietaryNeedsRepositoryAdapter = userDietaryNeedsRepositoryAdapter;
        this.userAllergiesRepositoryAdapter = userAllergiesRepositoryAdapter;
        this.findUserByEmailHibernateRepositoryAdapter = findUserByEmailHibernateRepositoryAdapter;
        this.findUserPreferencesByIdHibernateRepositoryAdapter = findUserPreferencesByIdHibernateRepositoryAdapter;
        this.getDietaryNeedsByIdHibernateRepositoryAdapter = getDietaryNeedsByIdHibernateRepositoryAdapter;
        this.getAllergiesByIdHibernateRepositoryAdapter = getAllergiesByIdHibernateRepositoryAdapter;
        this.getDietByIdHibernateRepositoryAdapter = getDietByIdHibernateRepositoryAdapter;
        this.getCookLevelByIdHibernateRepositoryAdapter = getCookLevelByIdHibernateRepositoryAdapter;
        this.getPlanByIdHibernateRepositoryAdapter = getPlanByIdHibernateRepositoryAdapter;
    }

    @Override
    public User execute(User user) {
        // Find existing user by email
        var existingUserOpt = findUserByEmailHibernateRepositoryAdapter.findByEmail(user.getEmail());
        if (existingUserOpt.isEmpty()) {
            throw new BadRequestException(ErrorDescription.USER_NOT_EXISTS.getValue());
        }

        var existingUser = existingUserOpt.get();

        // Update user basic fields if provided
        var userToUpdate = buildUpdatedUser(user, existingUser);
        var savedUser = createUserHibernateRepositoryAdapter.save(userToUpdate);

        // Update preferences if provided
        var savedPreferences = updatePreferences(user, savedUser);

        // Update dietary needs and allergies if provided
        updateDietaryNeeds(user, savedUser);
        updateAllergies(user, savedUser);

        // Build response
        User userResponse = savedUser.toDomain();
        if (savedPreferences != null) {
            userResponse.setPreferences(savedPreferences.toDomain());
        }
        userResponse.setDietaryNeeds(user.getDietaryNeeds());
        userResponse.setAllergies(user.getAllergies());

        return userResponse;
    }

    private UserHibernateModel buildUpdatedUser(User user, UserHibernateModel existingUser) {
        return new UserHibernateModel(
                existingUser.getId(),
                user.getName() != null ? user.getName() : existingUser.getName(),
                existingUser.getEmail(),
                existingUser.getPassword(),
                user.getPlan() != null ? 
                    getPlanByIdHibernateRepositoryAdapter.findById(user.getPlan().getId()).orElse(null) :
                    existingUser.getPlan(),
                existingUser.getActive(),
                existingUser.getCreatedAt(),
                LocalDateTime.now(),
                existingUser.getDeletedAt()
        );
    }

    private UserPreferencesHibernateModel updatePreferences(User user, UserHibernateModel savedUser) {
        if (user.getPreferences() == null) {
            return findUserPreferencesByIdHibernateRepositoryAdapter.findById(savedUser.getId()).orElse(null);
        }

        var existingPreferences = findUserPreferencesByIdHibernateRepositoryAdapter.findById(savedUser.getId());
        
        var preferencesToUpdate = new UserPreferencesHibernateModel(
                existingPreferences.map(UserPreferencesHibernateModel::getId).orElse(null),
                savedUser,
                user.getPreferences().getCookLevel() != null ? 
                    getCookLevelByIdHibernateRepositoryAdapter.findById(user.getPreferences().getCookLevel().getId()).orElse(null) :
                    existingPreferences.map(UserPreferencesHibernateModel::getCookLevel).orElse(null),
                user.getPreferences().getDiet() != null ? 
                    getDietByIdHibernateRepositoryAdapter.findById(user.getPreferences().getDiet().getId()).orElse(null) :
                    existingPreferences.map(UserPreferencesHibernateModel::getDiet).orElse(null)
        );

        return createUserPreferencesHibernateRepositoryAdapter.save(preferencesToUpdate);
    }

    private void updateDietaryNeeds(User user, UserHibernateModel savedUser) {
        if (user.getDietaryNeeds() != null) {
            // Delete existing dietary needs
            userDietaryNeedsRepositoryAdapter.deleteByUser_Id(savedUser.getId());
            
            // Get existing dietary need entities from database
            List<Integer> dietaryNeedIds = user.getDietaryNeeds().stream()
                    .map(DietaryNeed::getId)
                    .toList();
            
            List<DietaryNeedHibernateModel> existingDietaryNeeds = getDietaryNeedsByIdHibernateRepositoryAdapter.findByIdIn(dietaryNeedIds);
            
            // Create map for quick lookup
            Map<Integer, DietaryNeedHibernateModel> dietaryNeedMap = existingDietaryNeeds.stream()
                    .collect(Collectors.toMap(DietaryNeedHibernateModel::getId, Function.identity()));
            
            var dietaryNeeds = user.getDietaryNeeds()
                    .stream()
                    .map(dietaryNeed -> UserDietaryNeedsHibernateModel.builder()
                            .user(savedUser)
                            .dietaryNeed(dietaryNeedMap.get(dietaryNeed.getId()))
                            .build())
                    .toList();

            userDietaryNeedsRepositoryAdapter.saveAll(dietaryNeeds);
        }
    }

    private void updateAllergies(User user, UserHibernateModel savedUser) {
        if (user.getAllergies() != null) {
            // Delete existing allergies
            userAllergiesRepositoryAdapter.deleteByUser_Id(savedUser.getId());
            
            // Get existing allergy entities from database
            List<Integer> allergyIds = user.getAllergies().stream()
                    .map(Allergy::getId)
                    .toList();
            
            List<AllergyHibernateModel> existingAllergies = getAllergiesByIdHibernateRepositoryAdapter.findByIdIn(allergyIds);
            
            // Create map for quick lookup
            Map<Integer, AllergyHibernateModel> allergyMap = existingAllergies.stream()
                    .collect(Collectors.toMap(AllergyHibernateModel::getId, Function.identity()));
            
            var allergies = user.getAllergies()
                    .stream()
                    .map(allergy -> UserAllergiesHibernateModel.builder()
                            .user(savedUser)
                            .allergy(allergyMap.get(allergy.getId()))
                            .build())
                    .toList();

            userAllergiesRepositoryAdapter.saveAll(allergies);
        }
    }
} 