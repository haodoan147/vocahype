package com.vocahype.exception;



public class NoContentException extends RuntimeException {
    public NoContentException(final String message) {
        super(message);
    }
    public NoContentException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
