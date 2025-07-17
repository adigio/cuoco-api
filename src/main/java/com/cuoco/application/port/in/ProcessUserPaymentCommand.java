package com.cuoco.application.port.in;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

public interface ProcessUserPaymentCommand {
    void execute(Command command);

    @Data
    @Builder
    class Command {
        private String secret;
        private Map<String, Object> payload;
    }
}
