package com.cuoco.adapter.exception;

import com.cuoco.application.usecase.model.MessageError;

import java.util.List;

public class ForbiddenException extends AdapterException {
    public ForbiddenException(String description) {
        super(description);
    }

    public ForbiddenException(String description, List<MessageError> messages) {
        super(description, messages);
    }
}