package com.cuoco.application.usecase;

import com.cuoco.application.port.in.CreateUserCommand;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.port.out.CreateUserRepository;
import com.cuoco.application.port.out.UserExistsByUsernameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CreateUserUseCase implements CreateUserCommand {

    static final Logger log = LoggerFactory.getLogger(CreateUserUseCase.class);

    private final PasswordEncoder passwordEncoder;
    private final CreateUserRepository createUserRepository;
    private final UserExistsByUsernameRepository userExistsByUsernameRepository;

    public CreateUserUseCase(PasswordEncoder passwordEncoder, CreateUserRepository createUserRepository, UserExistsByUsernameRepository userExistsByUsernameRepository) {
        this.passwordEncoder = passwordEncoder;
        this.createUserRepository = createUserRepository;
        this.userExistsByUsernameRepository = userExistsByUsernameRepository;
    }

    public User execute(Command command) {
        log.info("Executing create user use case for username {}", command.getUser().getNombre());

        if(userExistsByUsernameRepository.execute(command.getUser().getNombre())) {
            log.info("User {} already exists", command.getUser().getNombre());
            throw new RuntimeException("El nombre de usuario ya existe.");
        }

        User userCreated = createUserRepository.execute(buildUser(command));

        userCreated.setPassword(null);

        return userCreated;
    }

    private User buildUser(CreateUserCommand.Command command) {
        String encriptedPassword = passwordEncoder.encode(command.getUser().getPassword());

        return new User(
                null,
                null,
                null,
                command.getUser().getNombre(),
                encriptedPassword
        );
    }
}
