package com.vocahype.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiError {

    @JsonIgnore
    private HttpStatus status;
    private Error errors;

    public ApiError(HttpStatus status, String title, String message) {
        this.status = status;
        this.errors = new Error(title, message);
    }

    public ApiError(HttpStatus status) {
        this.status = status;
    }

    public void setMessage(String defaultMessage) {
        this.errors.setTitle(defaultMessage);
    }

    @AllArgsConstructor
    @Getter
    @Setter
    class Error {
        private String title;
        private String detail;
    }
}
