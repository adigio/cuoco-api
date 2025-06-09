package com.cuoco.shared;


import com.cuoco.adapter.exception.ForbiddenException;
import com.cuoco.adapter.exception.NotAvailableException;
import com.cuoco.adapter.exception.UnprocessableException;
import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.exception.BusinessException;
import com.cuoco.application.exception.NotFoundException;
import com.cuoco.application.usecase.model.MessageError;
import com.cuoco.shared.model.ErrorDescription;
import com.cuoco.shared.model.exception.GenericException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<MessageError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new MessageError(error.getDefaultMessage()))
                .collect(Collectors.toList());

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ErrorDescription.VALIDATION_ERROR.getValue(),
                errors
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiErrorResponse> handle(ForbiddenException ex) {
        log.info(HttpStatus.FORBIDDEN.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.FORBIDDEN, ex);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handle(BadRequestException ex) {
        log.warn(HttpStatus.BAD_REQUEST.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(com.cuoco.adapter.exception.BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handle(com.cuoco.adapter.exception.BadRequestException ex) {
        log.info(HttpStatus.BAD_REQUEST.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handle(NotFoundException ex) {
        log.info(HttpStatus.NOT_FOUND.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(com.cuoco.adapter.exception.NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handle(com.cuoco.adapter.exception.NotFoundException ex) {
        log.info(HttpStatus.NOT_FOUND.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handle(BusinessException ex) {
        log.info(HttpStatus.CONFLICT.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.CONFLICT, ex);
    }

    @ExceptionHandler(UnprocessableException.class)
    public ResponseEntity<ApiErrorResponse> handle(UnprocessableException ex) {
        log.info(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.UNPROCESSABLE_ENTITY, ex);
    }

    @ExceptionHandler(NotAvailableException.class)
    public ResponseEntity<ApiErrorResponse> handle(NotAvailableException ex) {
        log.error(HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.SERVICE_UNAVAILABLE, ex);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiErrorResponse> handle(Throwable ex) {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex);
        ApiErrorResponse response = ApiErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage() != null ? ex.getMessage() : "Throwable exception")
                .errors(null)
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApiErrorResponse> buildResponseError(HttpStatus httpStatus, GenericException exception) {
        ApiErrorResponse response = ApiErrorResponse.builder()
                .status(httpStatus.value())
                .message(exception.getDescription())
                .errors(exception.getMessages())
                .build();
        return new ResponseEntity<>(response, httpStatus);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiErrorResponse {
        private int status;
        private String message;
        private List<MessageError> errors;
    }
}