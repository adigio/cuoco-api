package com.cuoco.application.usecase;

import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.in.UpdateUserProfileCommand;
import com.cuoco.application.port.out.GetAllergiesByIdRepository;
import com.cuoco.application.port.out.GetCookLevelByIdRepository;
import com.cuoco.application.port.out.GetDietByIdRepository;
import com.cuoco.application.port.out.GetDietaryNeedsByIdRepository;
import com.cuoco.application.port.out.GetPlanByIdRepository;
import com.cuoco.application.port.out.UpdateUserRepository;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.CookLevel;
import com.cuoco.application.usecase.model.Diet;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.Plan;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserPreferences;
import com.cuoco.shared.model.ErrorDescription;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class UpdateUserProfileUseCase implements UpdateUserProfileCommand {

    private final UpdateUserRepository updateUserRepository;
    private final GetPlanByIdRepository getPlanByIdRepository;
    private final GetDietByIdRepository getDietByIdRepository;
    private final GetCookLevelByIdRepository getCookLevelByIdRepository;
    private final GetDietaryNeedsByIdRepository getDietaryNeedsByIdRepository;
    private final GetAllergiesByIdRepository getAllergiesByIdRepository;

    public UpdateUserProfileUseCase(
            UpdateUserRepository updateUserRepository,
            GetPlanByIdRepository getPlanByIdRepository,
            GetDietByIdRepository getDietByIdRepository,
            GetCookLevelByIdRepository getCookLevelByIdRepository,
            GetDietaryNeedsByIdRepository getDietaryNeedsByIdRepository,
            GetAllergiesByIdRepository getAllergiesByIdRepository
    ) {
        this.updateUserRepository = updateUserRepository;
        this.getPlanByIdRepository = getPlanByIdRepository;
        this.getDietByIdRepository = getDietByIdRepository;
        this.getCookLevelByIdRepository = getCookLevelByIdRepository;
        this.getDietaryNeedsByIdRepository = getDietaryNeedsByIdRepository;
        this.getAllergiesByIdRepository = getAllergiesByIdRepository;
    }

    @Override
    @Transactional
    public User execute(Command command) {
        log.info("Executing update user profile use case for email {}", command.getUserEmail());

        List<DietaryNeed> dietaryNeeds = getDietaryNeeds(command);
        List<Allergy> allergies = getAllergies(command);
        Plan plan = getPlan(command.getPlanId());
        UserPreferences preferences = buildUserPreferences(command);

        User userToUpdate = buildUser(command, preferences, plan, dietaryNeeds, allergies);
        User updatedUser = updateUserRepository.execute(userToUpdate);

        log.info("User profile updated successfully for email {}", command.getUserEmail());
        return updatedUser;
    }

    private Plan getPlan(Integer planId) {
        if (planId == null) return null;
        
        Plan plan = getPlanByIdRepository.execute(planId);
        if (plan == null) {
            throw new BadRequestException(ErrorDescription.PLAN_NOT_EXISTS.getValue());
        }
        return plan;
    }

    private UserPreferences buildUserPreferences(Command command) {
        if (command.getCookLevelId() == null && command.getDietId() == null) {
            return null;
        }

        CookLevel cookLevel = getCookLevel(command.getCookLevelId());
        Diet diet = getDiet(command.getDietId());

        return UserPreferences.builder()
                .cookLevel(cookLevel)
                .diet(diet)
                .build();
    }

    private CookLevel getCookLevel(Integer cookLevelId) {
        if (cookLevelId == null) return null;
        
        CookLevel cookLevel = getCookLevelByIdRepository.execute(cookLevelId);
        if (cookLevel == null) {
            throw new BadRequestException(ErrorDescription.COOK_LEVEL_NOT_EXISTS.getValue());
        }
        return cookLevel;
    }

    private Diet getDiet(Integer dietId) {
        if (dietId == null) return null;
        
        Diet diet = getDietByIdRepository.execute(dietId);
        if (diet == null) {
            throw new BadRequestException(ErrorDescription.DIET_NOT_EXISTS.getValue());
        }
        return diet;
    }

    private List<DietaryNeed> getDietaryNeeds(Command command) {
        if (command.getDietaryNeeds() == null || command.getDietaryNeeds().isEmpty()) {
            return Collections.emptyList();
        }

        List<DietaryNeed> existingNeeds = getDietaryNeedsByIdRepository.execute(command.getDietaryNeeds());
        
        if (existingNeeds.size() != command.getDietaryNeeds().size()) {
            throw new BadRequestException(ErrorDescription.DIETARY_NEEDS_NOT_EXISTS.getValue());
        }

        return existingNeeds;
    }

    private List<Allergy> getAllergies(Command command) {
        if (command.getAllergies() == null || command.getAllergies().isEmpty()) {
            return Collections.emptyList();
        }

        List<Allergy> existingAllergies = getAllergiesByIdRepository.execute(command.getAllergies());
        
        if (existingAllergies.size() != command.getAllergies().size()) {
            throw new BadRequestException(ErrorDescription.ALLERGIES_NOT_EXISTS.getValue());
        }

        return existingAllergies;
    }

    private User buildUser(
            Command command,
            UserPreferences preferences,
            Plan plan,
            List<DietaryNeed> dietaryNeeds,
            List<Allergy> allergies
    ) {
        return User.builder()
                .email(command.getUserEmail())
                .name(command.getName())
                .plan(plan)
                .preferences(preferences)
                .dietaryNeeds(dietaryNeeds)
                .allergies(allergies)
                .build();
    }
}