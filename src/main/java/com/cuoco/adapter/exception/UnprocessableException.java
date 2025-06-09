package com.cuoco.adapter.exception;

import com.cuoco.application.usecase.model.MessageError;

import java.util.List;

public class UnprocessableException extends AdapterException {
    public UnprocessableException(String description) {
        super(description);
    }

    public UnprocessableException(String description, List<MessageError> messages) {
        super(description, messages);
    }
}