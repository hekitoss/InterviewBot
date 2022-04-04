package com.interview.validation;

import com.interview.entity.Rate;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import javax.xml.bind.ValidationException;

@Component
@Log4j
public class RateValidator implements Validator<Rate> {

    @SneakyThrows
    @Override
    public void validate(Rate rate){
        if(rate.getNumberOfEvaluations() != rate.getOne() + rate.getTwo() + rate.getThree() + rate.getFour() + rate.getFive()) {
            log.debug("rate not valid: " + rate);
            throw new ValidationException("rate not valid");
        }
    }
}
