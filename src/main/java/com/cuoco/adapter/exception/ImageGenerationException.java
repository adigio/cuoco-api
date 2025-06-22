package com.cuoco.adapter.exception;

import com.cuoco.shared.model.ErrorDescription;

public class ImageGenerationException extends AdapterException {

    public ImageGenerationException(String message) {
        super(message);
    }

    public ImageGenerationException(ErrorDescription errorDescription) {
        super(errorDescription.getValue());
    }
} 