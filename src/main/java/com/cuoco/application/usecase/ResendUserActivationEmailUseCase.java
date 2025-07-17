package com.cuoco.application.usecase;

import com.cuoco.application.port.in.ResendUserActivationEmailCommand;
import com.cuoco.application.port.out.ExistsUserByEmailRepository;
import com.cuoco.application.port.out.GetUserByEmailRepository;
import com.cuoco.application.port.out.SendConfirmationEmailRepository;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResendUserActivationEmailUseCase implements ResendUserActivationEmailCommand {

    private final ExistsUserByEmailRepository existsUserByEmailRepository;
    private final GetUserByEmailRepository getUserByEmailRepository;
    private final SendConfirmationEmailRepository sendConfirmationEmailRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void execute(String email) {
        log.info("Executing resend user activation email with {} ", email);

        if (existsUserByEmailRepository.execute(email)) {
            User user = getUserByEmailRepository.execute(email);
            String token = jwtUtil.generateActivationToken(user);
            sendConfirmationEmailRepository.execute(user, token);
        } else {
            log.info("Does not exists user with email {}. Resend email confirmation cancelled.", email);
        }
    }
}
