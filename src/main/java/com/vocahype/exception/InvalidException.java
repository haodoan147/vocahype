package com.vocahype.exception;

import lombok.Getter;
import org.springframework.web.client.RestClientException;

@Getter
public class InvalidException extends RestClientException {
    private String detail;
    public InvalidException(final String msg, final String detail) {
        super(msg);
        this.detail = detail;
    }
}
