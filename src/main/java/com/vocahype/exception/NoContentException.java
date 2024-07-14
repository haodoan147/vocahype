package com.vocahype.exception;

import lombok.Getter;

@Getter
public class NoContentException extends RuntimeException {
    private String detail;
    public NoContentException(final String message, final String detail) {
        super(message);
        this.detail = detail;
    }
}
