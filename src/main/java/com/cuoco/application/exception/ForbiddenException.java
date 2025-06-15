package com.cuoco.application.exception;

public class ForbiddenException extends BusinessException {
    public ForbiddenException(String description) {
        super(description, null);
    }
}
