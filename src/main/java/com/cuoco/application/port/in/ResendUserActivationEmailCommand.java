package com.cuoco.application.port.in;

public interface ResendUserActivationEmailCommand {
    void execute(String email);
}
