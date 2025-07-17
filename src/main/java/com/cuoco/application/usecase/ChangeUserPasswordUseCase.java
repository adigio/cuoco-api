package com.cuoco.application.usecase;

import com.cuoco.application.port.in.ChangeUserPasswordCommand;
import com.cuoco.application.port.out.ExistsUserByEmailRepository;
import com.cuoco.application.port.out.GetUserByEmailRepository;
import com.cuoco.application.port.out.GetUserByIdRepository;
import com.cuoco.application.port.out.SendResetPasswordConfirmationRepository;
import com.cuoco.application.port.out.UpdateUserRepository;
import com.cuoco.application.usecase.domainservice.UserDomainService;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChangeUserPasswordUseCase implements ChangeUserPasswordCommand {

    private final UserDomainService userDomainService;

    private final GetUserByIdRepository getUserByIdRepository;
    private final UpdateUserRepository updateUserRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void execute(Command command) {
        log.info("Executing change user password use case");

        User user;

        if(command.getToken() != null) {
            log.info("Change user password using token");

            Long id = Long.valueOf(jwtUtil.extractId(command.getToken()));
            user = getUserByIdRepository.execute(id);
        } else {
            log.info("Change user password for authenticated user");
            user = userDomainService.getCurrentUser();
        }

        String encriptedPassword = passwordEncoder.encode(command.getPassword());
        user.setPassword(encriptedPassword);

        updateUserRepository.execute(user);
    }
}
