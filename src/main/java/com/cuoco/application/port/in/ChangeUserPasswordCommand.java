package com.cuoco.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public interface ChangeUserPasswordCommand {
    void execute(Command command);

    @Data
    @Builder
    @AllArgsConstructor
    class Command {
        private String token;
        private String password;
    }
}
