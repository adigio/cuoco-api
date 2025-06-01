package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.User;
import lombok.Data;
import lombok.ToString;

public interface CreateUserCommand {

    User execute(Command command);

    @Data
    @ToString
    class Command {
        private final User user;

        public Command(User user) {
            this.user = user;
        }
        
    }
}