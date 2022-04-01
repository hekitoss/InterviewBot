package com.interview.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

public class CustomJwtException extends AuthenticationException {

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    private HttpStatus httpStatus;

    public CustomJwtException(String msg) {
        super(msg);
    }

    public CustomJwtException(String msg, HttpStatus httpStatus) {
        super(msg);
        this.httpStatus = httpStatus;
    }
}
