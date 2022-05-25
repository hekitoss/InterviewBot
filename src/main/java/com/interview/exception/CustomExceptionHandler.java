package com.interview.exception;

import com.interview.logger.Audit;
import io.jsonwebtoken.JwtException;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
@Log4j
public class CustomExceptionHandler {

    @Audit
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleException(NotFoundException e) {
        log.error("was catch error: " + e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @Audit
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleException(ValidationException e) {
        log.error("was catch error: " + e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @Audit
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleException(AuthenticationException e) {
        log.error("was catch error: " + e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>("invalid username/password combination", HttpStatus.UNAUTHORIZED);
    }

    @Audit
    @ExceptionHandler(CustomJwtException.class)
    public ResponseEntity<String> handleException(CustomJwtException e) {
        log.error("was catch error: " + e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @Audit
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<String> handleException(JwtException e) {
        log.error("was catch error: " + e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @Audit
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleException(IllegalArgumentException e) {
        log.error("was catch error: " + e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @Audit
    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleException(IOException e) {
        log.error("was catch error: " + e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @Audit
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Set<String>> handleValidationExceptions(
            ConstraintViolationException e) {
        log.error("was catch error: " + e.getMessage());
        Set<String> messages = new HashSet<>(e.getConstraintViolations().size());
        messages.addAll(e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList()));
        return  new ResponseEntity<>(messages, HttpStatus.BAD_REQUEST);
    }
}
