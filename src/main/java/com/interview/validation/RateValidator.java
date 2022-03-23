package com.interview.validation;

import com.interview.entity.Rate;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.xml.bind.ValidationException;

@Component
public class RateValidator {

    private static final Logger log = LogManager.getRootLogger();

    @SneakyThrows
    public void validate(Rate rate){
        if(rate.getNumberOfEvaluations() != rate.getOne() + rate.getTwo() + rate.getThree() + rate.getFour() + rate.getFive()) {
            log.debug("rate not valid: " + rate);
            throw new ValidationException("rate not valid");
        }
    }
}
