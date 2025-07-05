package com.cuoco.application.port.in;

public interface ActivateUserCommand {
    void execute(String email);

}
