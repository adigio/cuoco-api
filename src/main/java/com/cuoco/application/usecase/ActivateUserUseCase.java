package com.cuoco.application.usecase;

import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.in.ActivateUserCommand;
import com.cuoco.application.port.out.GetUserByEmailRepository;
import com.cuoco.application.port.out.UpdateUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivateUserUseCase implements ActivateUserCommand {
    private final GetUserByEmailRepository getUserByEmailRepository;
    private final UpdateUserRepository updateUserRepository;

    @Override
    @Transactional
    public void execute(String email) {
        var user = getUserByEmailRepository.execute(email);
        if (user == null) {
            throw new BadRequestException("Usuario no encontrado");
        }

        user.setActive(true);
        updateUserRepository.execute(user);
    }

}
