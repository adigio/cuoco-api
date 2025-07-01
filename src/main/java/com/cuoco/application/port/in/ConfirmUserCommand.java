package com.cuoco.application.port.in;

public interface ConfirmUserCommand {
    void execute(Long userId);
}
