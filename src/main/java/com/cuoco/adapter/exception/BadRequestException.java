package com.cuoco.adapter.exception;

import com.cuoco.application.usecase.model.MessageError;

import java.util.List;

public class BadRequestException extends AdapterException {
    public BadRequestException(String description) {
        super(description);
    }

    public BadRequestException(String description, List<MessageError> messages) {
        super(description, messages);
    }
}