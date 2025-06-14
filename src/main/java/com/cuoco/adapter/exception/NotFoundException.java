package com.cuoco.adapter.exception;

import com.cuoco.application.usecase.model.MessageError;

import java.util.List;

public class NotFoundException extends AdapterException {
    public NotFoundException(String description) {
        super(description);
    }

    public NotFoundException(String description, List<MessageError> messages) {
        super(description, messages);
    }
}