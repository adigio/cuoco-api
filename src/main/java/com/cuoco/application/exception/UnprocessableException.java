package com.cuoco.application.exception;

import com.cuoco.adapter.exception.AdapterException;
import com.cuoco.application.usecase.model.MessageError;

import java.util.List;

public class UnprocessableException extends BusinessException {
    public UnprocessableException(String description) {
        super(description, null);
    }

    public UnprocessableException(String description, List<MessageError> messages) {
        super(description, messages);
    }
}