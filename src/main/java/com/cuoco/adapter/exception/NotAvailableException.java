package com.cuoco.adapter.exception;

import com.cuoco.application.usecase.model.MessageError;

import java.util.List;

public class NotAvailableException extends AdapterException {
    public NotAvailableException(String description) {
        super(description);
    }

    public NotAvailableException(String description, List<MessageError> messages) {
        super(description, messages);
    }
}