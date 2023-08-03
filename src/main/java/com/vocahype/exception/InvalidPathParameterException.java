package com.vocahype.exception;



public class InvalidPathParameterException extends RuntimeException {
    public InvalidPathParameterException(final String message) {
        super(message);
    }
    public InvalidPathParameterException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
