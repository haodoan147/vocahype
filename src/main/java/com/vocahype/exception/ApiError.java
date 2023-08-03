package com.vocahype.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ApiError {

    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    public ApiError(final HttpStatus status) {
        this();
        this.status = status;
    }

    public ApiError(final HttpStatus status, final String message) {
        this();
        this.status = status;
        this.message = message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
