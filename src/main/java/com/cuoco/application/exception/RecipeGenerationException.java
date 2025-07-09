package com.cuoco.application.exception;

import com.cuoco.application.usecase.model.MessageError;

import java.util.Collections;
import java.util.List;

public class RecipeGenerationException extends BusinessException {
    
    public RecipeGenerationException(String message) {
        super(message, Collections.emptyList());
    }
    
    public RecipeGenerationException(String message, List<MessageError> errors) {
        super(message, errors);
    }
} 