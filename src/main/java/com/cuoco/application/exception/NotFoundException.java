package com.cuoco.application.exception;

import com.cuoco.application.usecase.model.MessageError;

import java.util.List;

public class NotFoundException extends BusinessException {
    public NotFoundException(String description, List<MessageError> messages) {
        super(description, messages);
    }
}
