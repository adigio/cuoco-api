package com.cuoco.application.usecase;

import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.in.UpdateUserProfileCommand;
import com.cuoco.application.port.out.GetAllergiesByIdRepository;
import com.cuoco.application.port.out.GetCookLevelByIdRepository;
import com.cuoco.application.port.out.GetDietByIdRepository;
import com.cuoco.application.port.out.GetDietaryNeedsByIdRepository;
import com.cuoco.application.port.out.GetUserByIdRepository;
import com.cuoco.application.port.out.UpdateUserRepository;
import com.cuoco.application.usecase.domainservice.UserDomainService;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.CookLevel;
import com.cuoco.application.usecase.model.Diet;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserPreferences;
import com.cuoco.shared.model.ErrorDescription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateUserProfileUseCase implements UpdateUserProfileCommand {

    private final UserDomainService userDomainService;
    private final GetUserByIdRepository getUserByIdRepository;
    private final UpdateUserRepository updateUserRepository;
    private final GetDietByIdRepository getDietByIdRepository;
    private final GetCookLevelByIdRepository getCookLevelByIdRepository;
    private final GetDietaryNeedsByIdRepository getDietaryNeedsByIdRepository;
    private final GetAllergiesByIdRepository getAllergiesByIdRepository;

    @Override
    public User execute(Command command) {
        User user = userDomainService.getCurrentUser();
        log.info("Executing update user use case with ID {}", user.getId());

        User existingUser = getUserByIdRepository.execute(user.getId());
        User userToUpdate = buildUpdateUser(existingUser, command);

        User updatedUser = updateUserRepository.execute(userToUpdate);

        log.info("User with ID {} updated successfully", user.getId());
        return updatedUser;
    }

    private User buildUpdateUser(User existingUser, Command command) {

        String updatedName = command.getName() != null ? command.getName() : existingUser.getName();

        return User.builder()
                .id(existingUser.getId())
                .name(updatedName)
                .email(existingUser.getEmail())
                .password(existingUser.getPassword())
                .active(existingUser.getActive())
                .plan(existingUser.getPlan())
                .preferences(buildUserPreferences(existingUser.getPreferences(), command))
                .dietaryNeeds(getUpdatedDietaryNeeds(command, existingUser.getDietaryNeeds()))
                .allergies(getUpdatedAllergies(command, existingUser.getAllergies()))
                .build();
    }

    private UserPreferences buildUserPreferences(UserPreferences existingUserPreferences, Command command) {
        Diet updatedDiet = command.getDietId() != null
                ? getDietByIdRepository.execute(command.getDietId())
                : existingUserPreferences.getDiet();

        CookLevel updatedCookLevel = command.getCookLevelId() != null
                ? getCookLevelByIdRepository.execute(command.getCookLevelId())
                : existingUserPreferences.getCookLevel();

        return UserPreferences.builder()
                .id(existingUserPreferences.getId())
                .cookLevel(updatedCookLevel)
                .diet(updatedDiet)
                .build();
    }

    private List<DietaryNeed> getUpdatedDietaryNeeds(Command command, List<DietaryNeed> existingDietaryNeeds) {
        List<Integer> ids = command.getDietaryNeeds();

        if (ids == null) {
            return existingDietaryNeeds;
        }

        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        List<DietaryNeed> existing = getDietaryNeedsByIdRepository.execute(ids);
        if (existing.size() != ids.size()) {
            throw new BadRequestException(ErrorDescription.DIETARY_NEEDS_NOT_EXISTS.getValue());
        }

        return existing;
    }

    private List<Allergy> getUpdatedAllergies(Command command, List<Allergy> existingAllergies) {
        List<Integer> ids = command.getAllergies();

        if (ids == null) {
            return existingAllergies;
        }

        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        List<Allergy> existing = getAllergiesByIdRepository.execute(ids);
        if (existing.size() != ids.size()) {
            throw new BadRequestException(ErrorDescription.ALLERGIES_NOT_EXISTS.getValue());
        }

        return existing;
    }
}