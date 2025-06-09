package com.cuoco.application.exception;

public class BadRequestException extends BusinessException {
    public BadRequestException(String description) {
        super(description, null);
    }
}
