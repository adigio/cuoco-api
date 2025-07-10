package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.User;

public interface SendConfirmationEmailRepository {
    void execute(User user, String token);
}
