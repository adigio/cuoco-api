package com.cuoco.application.usecase;

import com.cuoco.adapter.out.hibernate.repository.UserValidationRepository;
import com.cuoco.application.port.in.ConfirmUserCommand;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConfirmUserUseCase implements ConfirmUserCommand {

    private final UserValidationRepository userValidationRepository;

    @Override
    public void execute(Long userId) {
        userValidationRepository.setUserValid(userId);
    }
}
