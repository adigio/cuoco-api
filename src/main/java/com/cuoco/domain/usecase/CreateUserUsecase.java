package com.cuoco.domain.usecase;

import com.cuoco.domain.model.User;
import com.cuoco.domain.port.repository.CreateUserRepository;
import com.cuoco.domain.port.repository.UserExistsByUsernameRepository;
import org.springframework.stereotype.Component;

@Component
public class CreateUserUsecase {

    private final CreateUserRepository createUserRepository;
    private final UserExistsByUsernameRepository userExistsByUsernameRepository;

    public CreateUserUsecase(CreateUserRepository createUserRepository, UserExistsByUsernameRepository userExistsByUsernameRepository) {
        this.createUserRepository = createUserRepository;
        this.userExistsByUsernameRepository = userExistsByUsernameRepository;
    }

    public User execute(User user) {

        if(userExistsByUsernameRepository.execute(user.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya existe.");
        }

        User userResponse = createUserRepository.execute(user);

        return user;
    }
}
