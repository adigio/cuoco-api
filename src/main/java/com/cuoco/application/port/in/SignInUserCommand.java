package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.AuthenticatedUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public interface SignInUserCommand {

    AuthenticatedUser execute(Command command);

    @Data
    @Builder
    @AllArgsConstructor
    class Command {
        private final String email;
        private final String password;
    }
}
