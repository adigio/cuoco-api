package com.cuoco.application.usecase;

import com.cuoco.application.port.in.ResetUserPasswordConfirmationCommand;
import com.cuoco.application.port.out.ExistsUserByEmailRepository;
import com.cuoco.application.port.out.GetUserByEmailRepository;
import com.cuoco.application.port.out.SendResetPasswordConfirmationRepository;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResetUserPasswordConfirmationUseCase implements ResetUserPasswordConfirmationCommand {

    private final ExistsUserByEmailRepository existsUserByEmailRepository;
    private final GetUserByEmailRepository getUserByEmailRepository;
    private final SendResetPasswordConfirmationRepository sendResetPasswordConfirmationRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void execute(String email) {
        log.info("Executing password reset with email {} ", email);

        if (existsUserByEmailRepository.execute(email)) {
            User user = getUserByEmailRepository.execute(email);

            if (user.getActive()) {
                String token = jwtUtil.generateResetPasswordToken(user);
                sendResetPasswordConfirmationRepository.execute(user, token);
            } else {
                log.info("User with email {} is not actived. Reset password cancelled.", email);
            }
        } else {
            log.info("User does not exists with email {}. Reset password cancelled.", email);
        }
    }
}
