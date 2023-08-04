package com.vocahype.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@Log4j2
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex, final HttpHeaders headers,
                                                                  final HttpStatus status, final WebRequest request) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, error));
    }

    private ResponseEntity<Object> buildResponseEntity(final ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(UserFriendlyException.class)
    protected ResponseEntity<Object> handleUserFriendlyException(final UserFriendlyException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST, ex.getMessage(), ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(InvalidException.class)
    protected ResponseEntity<Object> handleInvalidException(final InvalidException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST, ex.getMessage(), ex.getDetail());
        return buildResponseEntity(apiError);
    }


    @ExceptionHandler(DBException.class)
    protected ResponseEntity<Object> handleDBException(final DBException ex) {
        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR, ex.getMessage(), ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(FailedLoginException.class)
    protected ResponseEntity<Object> handleFailedLoginException(final FailedLoginException ex) {
        ApiError apiError = new ApiError(UNAUTHORIZED, ex.getMessage(), ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ForbiddenException.class)
    protected ResponseEntity<Object> handleForbiddenException(final ForbiddenException ex) {
        ApiError apiError = new ApiError(FORBIDDEN, ex.getMessage(), ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(InvalidPathParameterException.class)
    protected ResponseEntity<Object> handleInvalidPathParameterException(final InvalidPathParameterException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST, ex.getMessage(), ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(InvalidRequestParametersException.class)
    protected ResponseEntity<Object> handleInvalidRequestParametersException(final InvalidRequestParametersException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST, ex.getMessage(), ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(NoContentException.class)
    protected ResponseEntity<Object> handleNoContentException(final NoContentException ex) {
        ApiError apiError = new ApiError(NO_CONTENT, ex.getMessage(), ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<Object> handleUnauthorizedException(final UnauthorizedException ex) {
        ApiError apiError = new ApiError(UNAUTHORIZED, ex.getMessage(), ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(InvalidSessionException.class)
    protected ResponseEntity<Object> handleInvalidSessionException(final InvalidSessionException ex) {
        ApiError apiError = new ApiError(UNAUTHORIZED, ex.getMessage(), ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(WebClientResponseException.class)
    protected ResponseEntity<Object> handleWebClientResponseException(final WebClientResponseException ex) {
        ApiError apiError = new ApiError(ex.getStatusCode(), ex.getMessage(), ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(DataAccessException.class)
    protected ResponseEntity<Object> handleWebClientResponseException(final DataAccessException ex) {
        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR, ex.getMessage(), ex.getMessage());
        log.error(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> onConstraintValidationException(final ConstraintViolationException e) {
        StringBuilder errors = new StringBuilder();
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            errors.append(violation.getPropertyPath().toString() + " : " + violation.getMessage() + ", ");
        }
        ApiError apiError = new ApiError(BAD_REQUEST);
        if (errors.length() < 2) {
            apiError.setMessage(e.getMessage());
        } else {
            apiError.setMessage(errors.toString().trim().substring(0, errors.length() - 2));
        }
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<Object> onUsernameNotFoundException(final UsernameNotFoundException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST, ex.getMessage(), ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<Object> onBadCredentialsException(final BadCredentialsException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST, "Invalid username or password", "Invalid username or password");
        return buildResponseEntity(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers,
                                                                  final HttpStatus status, final WebRequest request) {
        StringBuilder errors = new StringBuilder();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.append(fieldError.getField() + " : " + fieldError.getDefaultMessage() + ", ");
        }
        ApiError apiError = new ApiError(BAD_REQUEST);
        if (errors.length() < 2) {
            apiError.setMessage(ex.getGlobalError().getDefaultMessage());
        } else {
            apiError.setMessage(errors.toString().trim().substring(0, errors.length() - 2));
        }
        return buildResponseEntity(apiError);
    }

}
