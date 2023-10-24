package com.vocahype.exception;


import org.springframework.web.client.RestClientException;


public class UserFriendlyException extends RestClientException {
    private String detail;
    public UserFriendlyException(final String msg, final String detail) {
        super(msg);
        this.detail = detail;
    }

    public UserFriendlyException(final String msg) {
        super(msg);
    }

    public UserFriendlyException(final String msg, final Throwable ex) {
        super(msg, ex);
    }
}
