package com.cuoco.adapter.exception;

import com.cuoco.application.usecase.model.MessageError;
import com.cuoco.shared.model.exception.GenericException;

import java.util.List;

public class AdapterException extends GenericException {

    public AdapterException(String description) {
        super(description, null);
    }

    public AdapterException(String description, List<MessageError> messages) {
        super(description, messages);
    }
}