package com.cuoco.application.usecase;

import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.in.CreateUserCommand;
import com.cuoco.application.port.out.CreateUserRepository;
import com.cuoco.application.port.out.ExistsUserByEmailRepository;
import com.cuoco.application.port.out.GetAllergiesByIdRepository;
import com.cuoco.application.port.out.GetCookLevelByIdRepository;
import com.cuoco.application.port.out.GetDietByIdRepository;
import com.cuoco.application.port.out.GetDietaryNeedsByIdRepository;
import com.cuoco.application.port.out.GetPlanByIdRepository;
import com.cuoco.application.port.out.SendConfirmationEmailRepository;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.CookLevel;
import com.cuoco.application.usecase.model.Diet;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.Plan;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserPreferences;
import com.cuoco.application.utils.JwtUtil;
import com.cuoco.shared.model.ErrorDescription;
import com.cuoco.shared.utils.PlanConstants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateUserUseCase implements CreateUserCommand {

    private final PasswordEncoder passwordEncoder;
    private final CreateUserRepository createUserRepository;
    private final ExistsUserByEmailRepository existsUserByEmailRepository;
    private final GetPlanByIdRepository getPlanByIdRepository;
    private final GetDietByIdRepository getDietByIdRepository;
    private final GetCookLevelByIdRepository getCookLevelByIdRepository;
    private final GetDietaryNeedsByIdRepository getDietaryNeedsByIdRepository;
    private final GetAllergiesByIdRepository getAllergiesByIdRepository;
    private final SendConfirmationEmailRepository sendConfirmationEmailRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public User execute(Command command) {
        log.info("Executing create user use case for email {}", command.getEmail());

        if(existsUserByEmailRepository.execute(command.getEmail())) {
            log.info("Email {} already exists", command.getEmail());
            throw new BadRequestException(ErrorDescription.USER_DUPLICATED.getValue());
        }

        List<DietaryNeed> existingNeeds = getDietaryNeeds(command);
        List<Allergy> existingAlergies = getAllergies(command);

        Plan plan = getPlanByIdRepository.execute(PlanConstants.FREE.getValue());
        CookLevel cookLevel = getCookLevelByIdRepository.execute(command.getCookLevelId());
        Diet diet = getDietByIdRepository.execute(command.getDietId());

        UserPreferences preferencesToSave = buildUserPreferences(cookLevel, diet);
        User userToSave = buildUser(command, preferencesToSave, plan, existingNeeds, existingAlergies);

        User userCreated = createUserRepository.execute(userToSave);

        userCreated.setPassword(null);

        sendConfirmationEmail(userCreated);

        return userCreated;
    }

    private List<DietaryNeed> getDietaryNeeds(Command command) {
        List<DietaryNeed> existingNeeds = Collections.emptyList();

        if(command.getDietaryNeeds() != null && !command.getDietaryNeeds().isEmpty()) {

            existingNeeds = getDietaryNeedsByIdRepository.execute(command.getDietaryNeeds());

            if (existingNeeds.size() != command.getDietaryNeeds().size()) {
                throw new BadRequestException(ErrorDescription.DIETARY_NEEDS_NOT_EXISTS.getValue());
            }

        }
        return existingNeeds;
    }

    private List<Allergy> getAllergies(Command command) {
        List<Allergy> existingAlergies = Collections.emptyList();

        if(command.getAllergies() != null && !command.getAllergies().isEmpty()) {

            existingAlergies = getAllergiesByIdRepository.execute(command.getAllergies());

            if (existingAlergies.size() != command.getAllergies().size()) {
                throw new BadRequestException(ErrorDescription.ALLERGIES_NOT_EXISTS.getValue());
            }

        }
        return existingAlergies;
    }

    private User buildUser(
            CreateUserCommand.Command command,
            UserPreferences preferences,
            Plan plan,
            List<DietaryNeed> existingNeeds,
            List<Allergy> existingAlergies
    ) {

        String encriptedPassword = passwordEncoder.encode(command.getPassword());

        return User.builder()
                .name(command.getName())
                .email(command.getEmail())
                .password(encriptedPassword)
                .plan(plan)
                .active(false)
                .preferences(preferences)
                .dietaryNeeds(existingNeeds)
                .allergies(existingAlergies)
                .build();
    }

    private UserPreferences buildUserPreferences(CookLevel cookLevel, Diet diet) {
        return UserPreferences.builder()
                .diet(diet)
                .cookLevel(cookLevel)
                .build();
    }

    private void sendConfirmationEmail(User user) {
        String token = jwtUtil.generateActivationToken(user);
        sendConfirmationEmailRepository.execute(user, token);
    }
}
