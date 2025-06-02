package com.cuoco.application.usecase;

import com.cuoco.application.port.in.CreateUserCommand;
import com.cuoco.application.port.out.UserExistsByEmailRepository;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.port.out.CreateUserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CreateUserUseCase implements CreateUserCommand {

    static final Logger log = LoggerFactory.getLogger(CreateUserUseCase.class);

    private final PasswordEncoder passwordEncoder;
    private final CreateUserRepository createUserRepository;
    private final UserExistsByEmailRepository userExistsByEmailRepository;

    public CreateUserUseCase(PasswordEncoder passwordEncoder, CreateUserRepository createUserRepository, UserExistsByEmailRepository userExistsByEmailRepository) {
        this.passwordEncoder = passwordEncoder;
        this.createUserRepository = createUserRepository;
        this.userExistsByEmailRepository = userExistsByEmailRepository;
    }

    public User execute(Command command) {
        log.info("Executing create user use case for email {}", command.getUser().getEmail());

        if(userExistsByEmailRepository.execute(command.getUser().getEmail())) {
            log.info("Email {} already exists", command.getUser().getEmail());
            throw new RuntimeException("El email de usuario ya existe.");
        }

        User userCreated = createUserRepository.execute(buildUser(command));

        userCreated.setPassword(null);

        return userCreated;
    }

    private User buildUser(CreateUserCommand.Command command) {
        String encriptedPassword = passwordEncoder.encode(command.getUser().getPassword());
        User input = command.getUser();

        return new User(
                null,
                input.getName(),
                input.getEmail(),
                encriptedPassword,
                LocalDate.now(), // fecha actual
                input.getPlan() != null ? input.getPlan() : "free", // por defecto
                true,         // usuario válido por defecto
                input.getCookLevel(),
                input.getDiet()// puede ser null
        );
    }
}
