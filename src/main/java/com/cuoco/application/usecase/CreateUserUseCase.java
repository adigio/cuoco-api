package com.cuoco.application.usecase;

import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.in.CreateUserCommand;
import com.cuoco.application.port.out.*;
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
    private final FindDietaryNeedsByDescriptionRepository findDietaryNeedsByDescriptionRepository;
    private final FindAllergiesByDescriptionRepository findAllergiesByDescriptionRepository;

    public CreateUserUseCase(
            PasswordEncoder passwordEncoder,
            CreateUserRepository createUserRepository,
            UserExistsByEmailRepository userExistsByEmailRepository,
            GetPlanByIdRepository getPlanByIdRepository,
            GetDietByIdRepository getDietByIdRepository,
            GetCookLevelByIdRepository getCookLevelByIdRepository,
            FindDietaryNeedsByDescriptionRepository findDietaryNeedsByDescriptionRepository,
            FindAllergiesByDescriptionRepository findAllergiesByDescriptionRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.createUserRepository = createUserRepository;
        this.userExistsByEmailRepository = userExistsByEmailRepository;
        this.getPlanByIdRepository = getPlanByIdRepository;
        this.getDietByIdRepository = getDietByIdRepository;
        this.getCookLevelByIdRepository = getCookLevelByIdRepository;
        this.findDietaryNeedsByDescriptionRepository = findDietaryNeedsByDescriptionRepository;
        this.findAllergiesByDescriptionRepository = findAllergiesByDescriptionRepository;
    }

    @Transactional
    public User execute(Command command) {
        log.info("Executing create user use case for email {}", command.getEmail());

        if(userExistsByEmailRepository.execute(command.getEmail())) {
            log.info("Email {} already exists", command.getEmail());
            throw new BadRequestException(ErrorDescription.DUPLICATED.getValue());
        }

        List<DietaryNeed> existingNeeds = getDietaryNeeds(command);

        List<Allergy> existingAlergies = getAllergies(command);

        User userCreated = createUserRepository.execute(buildUser(command, existingNeeds, existingAlergies));

        userCreated.setPassword(null);

        return userCreated;
    }

    private List<DietaryNeed> getDietaryNeeds(Command command) {
        List<DietaryNeed> existingNeeds = Collections.emptyList();

        if(command.getDietaryNeeds() != null && !command.getDietaryNeeds().isEmpty()) {

            existingNeeds = findDietaryNeedsByDescriptionRepository.execute(command.getDietaryNeeds());

            if (existingNeeds.size() != command.getDietaryNeeds().size()) {
                throw new BadRequestException(ErrorDescription.PREFERENCES_NOT_EXISTS.getValue());
            }

        }
        return existingNeeds;
    }

    private List<Allergy> getAllergies(Command command) {
        List<Allergy> existingAlergies = Collections.emptyList();

        if(command.getAllergies() != null && !command.getAllergies().isEmpty()) {

            existingAlergies = findAllergiesByDescriptionRepository.execute(command.getAllergies());

            if (existingAlergies.size() != command.getAllergies().size()) {
                throw new BadRequestException(ErrorDescription.ALLERGIES_NOT_EXISTS.getValue());
            }

        }
        return existingAlergies;
    }

    private User buildUser(CreateUserCommand.Command command, List<DietaryNeed> existingNeeds, List<Allergy> existingAlergies) {
        String encriptedPassword = passwordEncoder.encode(command.getPassword());

        Plan plan = getPlanByIdRepository.execute(1);
        CookLevel cookLevel = getCookLevelByIdRepository.execute(1);
        Diet diet = getDietByIdRepository.execute(1);

        return new User(
                null,
                command.getName(),
                command.getEmail(),
                encriptedPassword,
                plan,
                true,
                new UserPreferences(
                        cookLevel,
                        diet
                ),
                null,
                existingNeeds,
                existingAlergies
        );
    }

}
