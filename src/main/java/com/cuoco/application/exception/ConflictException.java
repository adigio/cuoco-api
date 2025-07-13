package com.cuoco.application.exception;

public class ConflictException extends BusinessException {
    public ConflictException(String description) {
        super(description, null);
    }
}
