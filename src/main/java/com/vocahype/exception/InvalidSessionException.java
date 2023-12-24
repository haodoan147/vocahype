package com.vocahype.exception;

import org.springframework.web.client.RestClientException;

public class InvalidSessionException extends RestClientException {
    public InvalidSessionException(final String msg) {
        super(msg);
    }
    public InvalidSessionException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
