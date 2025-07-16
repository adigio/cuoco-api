package com.cuoco.application.port.in;

public interface ResetUserPasswordConfirmationCommand {
    void execute(String email);
}
