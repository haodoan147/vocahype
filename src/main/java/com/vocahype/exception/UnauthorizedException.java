package com.vocahype.exception;

import org.springframework.web.client.RestClientException;

public class UnauthorizedException extends RestClientException {
    public UnauthorizedException(final String msg) {
        super(msg);
    }
}
