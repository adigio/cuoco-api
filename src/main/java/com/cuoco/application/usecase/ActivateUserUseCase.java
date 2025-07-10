package com.cuoco.application.usecase;

import com.cuoco.application.port.in.ActivateUserCommand;
import com.cuoco.application.port.out.GetUserByIdRepository;
import com.cuoco.application.port.out.UpdateUserRepository;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.utils.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActivateUserUseCase implements ActivateUserCommand {

    private final GetUserByIdRepository getUserByIdRepository;
    private final UpdateUserRepository updateUserRepository;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public void execute(Command command) {
        log.info("Executing user activation with token {}", command.getToken());

        Long id = Long.valueOf(jwtUtil.extractId(command.getToken()));

        User user = getUserByIdRepository.execute(id);

        user.setActive(true);

        updateUserRepository.execute(user);

        log.info("Successfully activated user with ID {} and email {}", user.getId(), user.getEmail());
    }

}
