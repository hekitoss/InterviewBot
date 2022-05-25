package com.interview.exception;

import com.interview.logger.Audit;
import io.jsonwebtoken.JwtException;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.ui.Model;
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

@Log4j
@ControllerAdvice
public class CustomExceptionHandler {

    @Audit
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleException(NotFoundException e, Model model) {
        log.error("was catch error: " + e.getMessage());
        e.printStackTrace();
        model.addAttribute("errors", e.getMessage());
        return "error";
    }

    @Audit
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public String handleException(ValidationException e, Model model) {
        log.error("was catch error: " + e.getMessage());
        e.printStackTrace();
        model.addAttribute("errors", e.getMessage());
        return "error";
    }

    @Audit
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public String handleException(AuthenticationException e, Model model) {
        log.error("was catch error: " + e.getMessage());
        e.printStackTrace();
        model.addAttribute("errors", e.getMessage());
        return "error";
    }

    @Audit
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(CustomJwtException.class)
    public String handleException(CustomJwtException e, Model model) {
        log.error("was catch error: " + e.getMessage());
        e.printStackTrace();
        model.addAttribute("errors", e.getMessage());
        return "error";
    }

    @Audit
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JwtException.class)
    public String handleException(JwtException e, Model model) {
        log.error("was catch error: " + e.getMessage());
        e.printStackTrace();
        model.addAttribute("errors", e.getMessage());
        return "error";
    }

    @Audit
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleException(IllegalArgumentException e, Model model) {
        log.error("was catch error: " + e.getMessage());
        e.printStackTrace();
        model.addAttribute("errors", e.getMessage());
        return "error";
    }

    @Audit
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IOException.class)
    public String handleException(IOException e, Model model) {
        log.error("was catch error: " + e.getMessage());
        e.printStackTrace();
        model.addAttribute("errors", e.getMessage());
        return "error";
    }

    @Audit
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public String handleValidationExceptions(ConstraintViolationException e, Model model) {
        log.error("was catch error: " + e.getMessage());
        Set<String> messages = new HashSet<>(e.getConstraintViolations().size());
        messages.addAll(e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList()));
        model.addAttribute("errors", messages);
        return "error";
    }
}
