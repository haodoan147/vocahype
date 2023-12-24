package com.vocahype.exception;



public class FailedLoginException extends RuntimeException {
    public FailedLoginException(final String message) {
        super(message);
    }
    public FailedLoginException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
