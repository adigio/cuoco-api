package com.cuoco.application.usecase;

import com.cuoco.application.port.in.CreateUserCommand;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.port.out.CreateUserRepository;
import com.cuoco.application.port.out.UserExistsByUsernameRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CreateUserUseCase implements CreateUserCommand {

    private final PasswordEncoder passwordEncoder;
    private final CreateUserRepository createUserRepository;
    private final UserExistsByUsernameRepository userExistsByUsernameRepository;

    public CreateUserUseCase(PasswordEncoder passwordEncoder, CreateUserRepository createUserRepository, UserExistsByUsernameRepository userExistsByUsernameRepository) {
        this.passwordEncoder = passwordEncoder;
        this.createUserRepository = createUserRepository;
        this.userExistsByUsernameRepository = userExistsByUsernameRepository;
    }

    public User execute(Command command) {

        if(userExistsByUsernameRepository.execute(command.getUser().getUsername())) {
            throw new RuntimeException("El nombre de usuario ya existe.");
        }

        return createUserRepository.execute(buildUser(command));
    }

    private User buildUser(CreateUserCommand.Command command) {
        String encriptedPassword = passwordEncoder.encode(command.getUser().getPassword());

        return new User(
                null,
                null,
                null,
                command.getUser().getUsername(),
                encriptedPassword,
                null
        );
    }
}
