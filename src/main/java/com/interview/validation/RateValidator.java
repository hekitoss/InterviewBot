package com.interview.validation;

import com.interview.dao.Rate;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.xml.bind.ValidationException;

@Component
public class RateValidator {

    @SneakyThrows
    public void validate(Rate rate){
        if(rate.getNumberOfEvaluations() != rate.getOne() + rate.getTwo() + rate.getThree() + rate.getFour() + rate.getFive()) {
            throw new ValidationException("rate not valid");
        }
    }
}
