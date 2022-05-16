package com.interview.exception;

import com.interview.logger.Audit;
import io.jsonwebtoken.JwtException;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.xml.bind.ValidationException;
import java.io.IOException;

@ControllerAdvice
@Log4j
public class CustomExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @Audit
    public ResponseEntity<String> handleException(NotFoundException e) {
        log.error("was catch error: " + e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    @Audit
    public ResponseEntity<String> handleException(ValidationException e) {
        log.error("was catch error: " + e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    @Audit
    public ResponseEntity<String> handleException(AuthenticationException e) {
        log.error("was catch error: " + e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>("invalid username/password combination", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CustomJwtException.class)
    @Audit
    public ResponseEntity<String> handleException(CustomJwtException e) {
        log.error("was catch error: " + e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.class)
    @Audit
    public ResponseEntity<String> handleException(JwtException e) {
        log.error("was catch error: " + e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @Audit
    public ResponseEntity<String> handleException(IllegalArgumentException e) {
        log.error("was catch error: " + e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    @Audit
    public ResponseEntity<String> handleException(IOException e) {
        log.error("was catch error: " + e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
