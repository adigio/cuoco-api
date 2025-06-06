package com.cuoco.application.usecase;

import com.cuoco.application.port.in.CreateUserCommand;
import com.cuoco.application.port.out.FindDietaryNeedsByNameRepository;
import com.cuoco.application.port.out.CreateUserDietaryNeedRepository;
import com.cuoco.application.port.out.UserExistsByEmailRepository;
import com.cuoco.application.usecase.model.DietaryNeeds;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.port.out.CreateUserRepository;

import com.cuoco.application.usecase.model.UserPreferences;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class CreateUserUseCase implements CreateUserCommand {

    static final Logger log = LoggerFactory.getLogger(CreateUserUseCase.class);

    private final PasswordEncoder passwordEncoder;
    private final CreateUserRepository createUserRepository;
    private final UserExistsByEmailRepository userExistsByEmailRepository;
    private final CreateUserDietaryNeedRepository createUserDietaryNeedRepository;
    private final FindDietaryNeedsByNameRepository findDietaryNeedsByNameRepository;


    public CreateUserUseCase(PasswordEncoder passwordEncoder,
                             CreateUserRepository createUserRepository,
                             UserExistsByEmailRepository userExistsByEmailRepository,
                             CreateUserDietaryNeedRepository createUserDietaryNeedRepository,
                             FindDietaryNeedsByNameRepository findDietaryNeedsByNameRepository) {
        this.passwordEncoder = passwordEncoder;
        this.createUserRepository = createUserRepository;
        this.userExistsByEmailRepository = userExistsByEmailRepository;
        this.createUserDietaryNeedRepository = createUserDietaryNeedRepository;
        this.findDietaryNeedsByNameRepository = findDietaryNeedsByNameRepository;
    }

    @Transactional
    public User execute(Command command) {
        log.info("Executing create user use case for email {}", command.getEmail());

        if(userExistsByEmailRepository.execute(command.getEmail())) {
            log.info("Email {} already exists", command.getEmail());
            throw new RuntimeException("El email de usuario ya existe.");
        }

        List<DietaryNeeds> existingNeeds = Collections.emptyList();

        if(command.getDietaryNeeds() != null && !command.getDietaryNeeds().isEmpty()) {
            existingNeeds = findDietaryNeedsByNameRepository.execute(command.getDietaryNeeds());
            if (existingNeeds.size() != command.getDietaryNeeds().size()) {
                throw new RuntimeException("Las preferencias de usuario no existen.");
            }
        }

        User userToSave = buildUser(command, existingNeeds);

        User userCreated = createUserRepository.execute(userToSave);

        createUserDietaryNeedRepository.execute(userCreated.getId(), existingNeeds);

        userCreated.setPassword(null);

        return userCreated;
    }


    private User buildUser(CreateUserCommand.Command command, List<DietaryNeeds> existingNeeds) {
        String encriptedPassword = passwordEncoder.encode(command.getPassword());


        return new User(
                null,
                command.getName(),
                command.getEmail(),
                encriptedPassword,
                LocalDate.now(),
                command.getPlan() != null ? command.getPlan() : "free",
                true,
                new UserPreferences(
                        command.getCookLevel(),
                        command.getDiet(),
                        existingNeeds
                )
        );
    }

}
