package com.cuoco.application.usecase;

import com.cuoco.application.port.in.CreateUserCommand;
import com.cuoco.application.port.in.CreateUserDietaryCommand;
import com.cuoco.application.port.out.CreateUserDietaryRepository;
import com.cuoco.application.port.out.SaveUserDietaryNeedRepository;
import com.cuoco.application.port.out.UserExistsByEmailRepository;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.port.out.CreateUserRepository;

import com.cuoco.application.usecase.model.UserDietaryNeeds;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class CreateUserUseCase implements CreateUserCommand {

    static final Logger log = LoggerFactory.getLogger(CreateUserUseCase.class);

    private final PasswordEncoder passwordEncoder;
    private final CreateUserRepository createUserRepository;
    private final UserExistsByEmailRepository userExistsByEmailRepository;
    private final SaveUserDietaryNeedRepository saveUserDietaryNeedRepository;


    public CreateUserUseCase(PasswordEncoder passwordEncoder,
                             CreateUserRepository createUserRepository,
                             UserExistsByEmailRepository userExistsByEmailRepository,
                             SaveUserDietaryNeedRepository saveUserDietaryNeedRepository) {
        this.passwordEncoder = passwordEncoder;
        this.createUserRepository = createUserRepository;
        this.userExistsByEmailRepository = userExistsByEmailRepository;
        this.saveUserDietaryNeedRepository = saveUserDietaryNeedRepository;

    }

    @Transactional
    public User execute(Command command) {
        log.info("Executing create user use case for email {}", command.getUser().getEmail());

        if(userExistsByEmailRepository.execute(command.getUser().getEmail())) {
            log.info("Email {} already exists", command.getUser().getEmail());
            throw new RuntimeException("El email de usuario ya existe.");
        }

        User userToSave = buildUserForDatabase(command);

        User userCreated = createUserRepository.execute(userToSave);

        List<String> dietaryNeedNames = extractDietaryNeedNames(command.getUser());

        if (!dietaryNeedNames.isEmpty()) {
            saveUserDietaryNeedRepository.execute(userCreated, dietaryNeedNames);
            log.info("Saved {} dietary needs for user with id {}", dietaryNeedNames.size(), userCreated.getId());
        }

        userCreated.setDietaryNeeds(dietaryNeedNames);

        userCreated.setPassword(null);

        return userCreated;
    }


    private User buildUserForDatabase(CreateUserCommand.Command command) {
        String encriptedPassword = passwordEncoder.encode(command.getUser().getPassword());
        User input = command.getUser();

        return new User(
                null,
                input.getName(),
                input.getEmail(),
                encriptedPassword,
                LocalDate.now(),
                input.getPlan() != null ? input.getPlan() : "free",
                true,
                input.getCookLevel(),
                input.getDiet(),
                null  // No guardar dietaryNeeds en la tabla User
        );
    }

//    private User buildUser(CreateUserCommand.Command command) {
//        String encriptedPassword = passwordEncoder.encode(command.getUser().getPassword());
//        User input = command.getUser();
//
//        return new User(
//                null,
//                input.getName(),
//                input.getEmail(),
//                encriptedPassword,
//                LocalDate.now(), // fecha actual
//                input.getPlan() != null ? input.getPlan() : "free", // por defecto
//                true,         // usuario válido por defecto
//                input.getCookLevel(),
//                input.getDiet(),
//                input.getDietaryNeeds()
//        );// puede ser null
//    }

    private List<String> extractDietaryNeedNames(User user) {
        List<String> dietaryNeeds = new ArrayList<>();

        if (user.getDietaryNeeds() != null && !user.getDietaryNeeds().isEmpty()) {
            dietaryNeeds.addAll(user.getDietaryNeeds());
        }

        return dietaryNeeds;
    }

}
