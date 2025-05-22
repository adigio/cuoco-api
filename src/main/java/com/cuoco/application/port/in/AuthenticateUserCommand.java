package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.User;

public interface AuthenticateUserCommand {

    User execute(Command command);

    class Command {
        private final User user;

        public Command(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
        }

        @Override
        public String toString() {
            return "Command(user=" + user + ")";
        }
    }
}
