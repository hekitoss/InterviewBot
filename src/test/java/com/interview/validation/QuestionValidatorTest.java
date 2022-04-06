package com.interview.validation;

import com.interview.entity.Question;
import com.interview.entity.Rate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionValidatorTest {

    private final Question validQuestion;

    @Autowired
    QuestionValidator questionValidator;

    {
        validQuestion = new Question()
                .setText("text")
                .setAnswer("answer")
                .setId(1L)
                .setCreationTime(LocalDateTime.now())
                .setRate(new Rate()
                        .setId(1L)
                        .setFive(1)
                        .setThree(1)
                        .setNumberOfEvaluations(2)
                );
    }

    @Test
    public void validateNotValidQuest(){
        validQuestion.setCreationTime(LocalDateTime.now().plusDays(1));

        assertThrows(ValidationException.class,
                () -> questionValidator.validate(validQuestion));
    }
}