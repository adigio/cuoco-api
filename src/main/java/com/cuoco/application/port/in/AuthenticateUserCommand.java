package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.AuthenticatedUser;

public interface AuthenticateUserCommand {
    AuthenticatedUser execute(Command command);

    class Command {
        private final String authHeader;

        public Command(String authHeader) {
            this.authHeader = authHeader;
        }

        public String getAuthHeader() {
            return authHeader;
        }

        @Override
        public String toString() {
            return "Command{" +
                    "authHeader='" + authHeader + '\'' +
                    '}';
        }
    }
}
