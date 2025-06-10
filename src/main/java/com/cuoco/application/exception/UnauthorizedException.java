package com.cuoco.application.exception;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(String description) {
        super(description, null);
    }
}
