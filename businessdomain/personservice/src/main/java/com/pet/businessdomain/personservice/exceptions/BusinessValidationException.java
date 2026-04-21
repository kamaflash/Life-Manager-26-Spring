package com.pet.businessdomain.personservice.exceptions;

import lombok.Getter;

@Getter
public class BusinessValidationException extends RuntimeException {

    private final String errorCode;
    private final String fieldName;

    public BusinessValidationException(String message) {
        super(message);
        this.errorCode = "VALIDATION_ERROR";
        this.fieldName = null;
    }

    public BusinessValidationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.fieldName = null;
    }

    public BusinessValidationException(String message, String errorCode, String fieldName) {
        super(message);
        this.errorCode = errorCode;
        this.fieldName = fieldName;
    }

    public BusinessValidationException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "VALIDATION_ERROR";
        this.fieldName = null;
    }
}