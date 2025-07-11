package com.cuoco.application.usecase;

import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.in.CreateUserCommand;
import com.cuoco.application.port.out.CreateUserRepository;
import com.cuoco.application.port.out.GetAllergiesByIdRepository;
import com.cuoco.application.port.out.GetCookLevelByIdRepository;
import com.cuoco.application.port.out.GetDietByIdRepository;
import com.cuoco.application.port.out.GetDietaryNeedsByIdRepository;
import com.cuoco.application.port.out.GetPlanByIdRepository;
import com.cuoco.application.port.out.UserExistsByEmailRepository;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.CookLevel;
import com.cuoco.application.usecase.model.Diet;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.Plan;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserPreferences;
import com.cuoco.shared.model.ErrorDescription;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CreateUserUseCase implements CreateUserCommand {

    static final Logger log = LoggerFactory.getLogger(CreateUserUseCase.class);

    private final PasswordEncoder passwordEncoder;
    private final CreateUserRepository createUserRepository;
    private final UserExistsByEmailRepository userExistsByEmailRepository;
    private final GetPlanByIdRepository getPlanByIdRepository;
    private final GetDietByIdRepository getDietByIdRepository;
    private final GetCookLevelByIdRepository getCookLevelByIdRepository;
    private final GetDietaryNeedsByIdRepository getDietaryNeedsByIdRepository;
    private final GetAllergiesByIdRepository getAllergiesByIdRepository;

    public CreateUserUseCase(
            PasswordEncoder passwordEncoder,
            CreateUserRepository createUserRepository,
            UserExistsByEmailRepository userExistsByEmailRepository,
            GetPlanByIdRepository getPlanByIdRepository,
            GetDietByIdRepository getDietByIdRepository,
            GetCookLevelByIdRepository getCookLevelByIdRepository,
            GetDietaryNeedsByIdRepository getDietaryNeedsByIdRepository,
            GetAllergiesByIdRepository getAllergiesByIdRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.createUserRepository = createUserRepository;
        this.userExistsByEmailRepository = userExistsByEmailRepository;
        this.getPlanByIdRepository = getPlanByIdRepository;
        this.getDietByIdRepository = getDietByIdRepository;
        this.getCookLevelByIdRepository = getCookLevelByIdRepository;
        this.getDietaryNeedsByIdRepository = getDietaryNeedsByIdRepository;
        this.getAllergiesByIdRepository = getAllergiesByIdRepository;
    }

    @Transactional
    public User execute(Command command) {
        log.info("Executing create user use case for email {}", command.getEmail());

        if(userExistsByEmailRepository.execute(command.getEmail())) {
            log.info("Email {} already exists", command.getEmail());
            throw new BadRequestException(ErrorDescription.USER_DUPLICATED.getValue());
        }

        List<DietaryNeed> existingNeeds = getDietaryNeeds(command);
        List<Allergy> existingAlergies = getAllergies(command);

        Plan plan = getPlan(command.getPlanId());
        CookLevel cookLevel = getCookLevel(command.getCookLevelId());
        Diet diet = getDiet(command.getDietId());

        UserPreferences preferencesToSave = buildUserPreferences(cookLevel, diet);
        User userToSave = buildUser(command, preferencesToSave, plan, existingNeeds, existingAlergies);

        User userCreated = createUserRepository.execute(userToSave);

        userCreated.setPassword(null);

        return userCreated;
    }

    private Plan getPlan(Integer planId) {
        Plan plan = getPlanByIdRepository.execute(planId);
        if(plan == null) throw new BadRequestException(ErrorDescription.PLAN_NOT_EXISTS.getValue());
        return plan;
    }

    private Diet getDiet(Integer dietId) {
        Diet diet = getDietByIdRepository.execute(dietId);
        if(diet == null) throw new BadRequestException(ErrorDescription.DIET_NOT_EXISTS.getValue());
        return diet;
    }

    private CookLevel getCookLevel(Integer cookLevelId) {
        CookLevel cookLevel = getCookLevelByIdRepository.execute(cookLevelId);
        if(cookLevel == null) throw new BadRequestException(ErrorDescription.COOK_LEVEL_NOT_EXISTS.getValue());
        return cookLevel;
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
                .active(true)
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

}
