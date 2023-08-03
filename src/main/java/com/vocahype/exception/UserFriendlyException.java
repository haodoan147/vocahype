package com.vocahype.exception;


import org.springframework.web.client.RestClientException;


public class UserFriendlyException extends RestClientException {

    public UserFriendlyException(final String msg) {
        super(msg);
    }

    public UserFriendlyException(final String msg, final Throwable ex) {
        super(msg, ex);
    }
}
