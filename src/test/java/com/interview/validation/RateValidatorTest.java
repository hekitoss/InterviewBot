package com.interview.validation;

import com.interview.dao.Rate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.bind.ValidationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RateValidatorTest {
    private final Rate validRate;

    @Autowired
    RateValidator rateValidator;

    {
        validRate = new Rate().setId(1L)
                .setFive(1)
                .setThree(1)
                .setNumberOfEvaluations(2);
    }

    @Test
    public void validateNotValidQuest(){
        validRate.setNumberOfEvaluations(3);

        assertThrows(ValidationException.class,
                () -> rateValidator.validate(validRate));
    }
}