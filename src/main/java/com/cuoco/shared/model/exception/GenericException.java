package com.cuoco.shared.model.exception;

import com.cuoco.application.usecase.model.MessageError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GenericException extends RuntimeException {

    private String description;
    private List<MessageError> messages;
}
