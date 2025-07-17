package com.cuoco.application.exception;

public class NotAvailableException extends BusinessException {
    public NotAvailableException(String description) { super(description, null); }
}
