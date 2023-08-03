package com.vocahype.exception;

import org.springframework.web.client.RestClientException;

public class InvalidException extends RestClientException {
    public InvalidException(final String msg) {
        super(msg);
    }
}
