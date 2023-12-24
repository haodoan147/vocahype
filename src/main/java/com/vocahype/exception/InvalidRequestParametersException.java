package com.vocahype.exception;



public class InvalidRequestParametersException extends RuntimeException {
    public InvalidRequestParametersException(final String message) {
        super(message);
    }
    public InvalidRequestParametersException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
