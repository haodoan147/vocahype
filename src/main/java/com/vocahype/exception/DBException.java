package com.vocahype.exception;

public class DBException extends RuntimeException {
    public DBException(final String message) {
        super(message);
    }
    public DBException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
