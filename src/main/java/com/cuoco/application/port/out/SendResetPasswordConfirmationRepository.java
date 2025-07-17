package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.User;

public interface SendResetPasswordConfirmationRepository {
    void execute(User user, String token);
}
