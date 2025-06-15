package com.cuoco.application.exception;

import com.cuoco.application.usecase.model.MessageError;
import com.cuoco.shared.model.exception.GenericException;

import java.util.List;

public class BusinessException extends GenericException {
    public BusinessException(String description, List<MessageError> messages) {
        super(description, messages);
    }
}
