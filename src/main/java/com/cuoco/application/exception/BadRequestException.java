package com.cuoco.application.exception;

import com.cuoco.application.usecase.model.MessageError;

import java.util.List;

public class BadRequestException extends BusinessException {
    public BadRequestException(String description) {
        super(description, null);
    }
}
